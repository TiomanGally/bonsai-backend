package de.gally.bonsai.adapter

import de.gally.bonsai.domain.Picture
import de.gally.bonsai.domain.usecases.PictureManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.io.File
import java.nio.file.Files
import java.time.LocalDate
import java.util.UUID

/**
 * First I thought it would be cool to have everything in my database. Although saving a blob (binary large object) in a postgres instance comes with costs.
 * Reading from the filesystem is faster in my scenario.
 */
@Repository
@ConditionalOnProperty(value = ["bonsai.pictures.store-pictures-in-database"], havingValue = "true", matchIfMissing = false)
class PictureDatabaseManagerService(
    private val jdbcTemplate: JdbcTemplate,
) : PictureManager {

    private val rowMapper = RowMapper { rs, _ ->
        Picture(
            uuid = UUID.fromString(rs.getString("uuid")),
            fileName = rs.getString("file_name"),
            content = rs.getBytes("content"),
            createdAt = rs.getString("created_at").let { LocalDate.parse(it) }
        )
    }

    override fun addPicture(bonsaiUuid: UUID, fileName: String, picture: ByteArray): Picture {
        val sql = "insert into pictures (uuid, bonsai_uuid, file_name, content) values (?, ?, ?, ?)"
        val uuid = UUID.randomUUID()
        jdbcTemplate.update(sql, uuid, bonsaiUuid, fileName, picture)
        return Picture(uuid, fileName, picture)
    }

    override fun deletePicture(bonsaiUuid: UUID, pictureUuid: UUID) {
        val sql = "DELETE FROM pictures WHERE uuid = ? and bonsai_uuid = ?"
        jdbcTemplate.update(sql, pictureUuid, bonsaiUuid)
    }

    override fun getAllPictureUuids(bonsaiUuid: UUID, page: Int, pageSize: Int): List<UUID> {
        val offset = page.takeIf { page != 0 } ?: 1
        return jdbcTemplate
            .queryForList(
                """
                select uuid
                from pictures
                where bonsai_uuid = ?
                order by created_at desc
                limit ? offset (? - 1) * ?
            """.trimIndent(), String::class.java, bonsaiUuid, pageSize, offset, pageSize
            )
            .map { UUID.fromString(it) }
    }

    override fun getPictureByUuid(bonsaiUuid: UUID, pictureUuid: UUID): Picture? {
        val sql = "select uuid, file_name, content, created_at from pictures where uuid = ? and bonsai_uuid = ?"

        return jdbcTemplate.query(sql, rowMapper, pictureUuid, bonsaiUuid).firstOrNull()
    }
}

/**
 * As my server won't be running all the time I still want to access the pictures of the trees, so I thought it would be super comfortable to just persist them
 * on my filesystem without the need to transform a bytearray to a picture or to login somewhere.
 */
@Repository
@ConditionalOnProperty(value = ["bonsai.pictures.store-pictures-in-database"], havingValue = "false", matchIfMissing = true)
class PictureLocalFileSystemManagerService : PictureManager {

    private companion object {
        const val BASE_PICTURE_DIR = "pictures"
    }

    private val path: (bonsaiUuid: UUID, pictureUuid: UUID, fileName: String) -> String = { bonsaiUuid, pictureUuid, fileName ->
        "$BASE_PICTURE_DIR/$bonsaiUuid/${pictureUuid}_${LocalDate.now()}.${fileName.substringAfterLast(".")}"
    }

    override fun addPicture(bonsaiUuid: UUID, fileName: String, picture: ByteArray): Picture {
        val pictureUuid = UUID.randomUUID()
        val path = path(bonsaiUuid, pictureUuid, fileName)
        val file = File(path)
        file.parentFile.mkdirs()
        file.writeBytes(picture)
        return Picture(pictureUuid, fileName, picture)
    }

    override fun deletePicture(bonsaiUuid: UUID, pictureUuid: UUID) {
        val file = File("$BASE_PICTURE_DIR/$bonsaiUuid")
            .listFiles { file -> file.id() == pictureUuid }
            ?.firstOrNull()

        if (file != null) {
            Files.delete(file.toPath())
        }
    }

    override fun getAllPictureUuids(bonsaiUuid: UUID, page: Int, pageSize: Int): List<UUID> {
        return File("$BASE_PICTURE_DIR/$bonsaiUuid").listFiles()?.map { it.id() } ?: emptyList()
    }

    override fun getPictureByUuid(bonsaiUuid: UUID, pictureUuid: UUID): Picture? {
        return File("$BASE_PICTURE_DIR/$bonsaiUuid")
            .listFiles()
            ?.firstOrNull { it.id() == pictureUuid }
            ?.let { Picture(pictureUuid, it.name, it.readBytes(), it.createdAt()) }
    }

    private fun File.id(): UUID = this.name.substringBeforeLast("_").let { UUID.fromString(it) }
    private fun File.createdAt(): LocalDate = this.name
        .substringAfterLast("_")
        .substringBeforeLast(".")
        .let { LocalDate.parse(it) }
}
