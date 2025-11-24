package de.gally.bonsai.adapter

import de.gally.bonsai.domain.Note
import de.gally.bonsai.domain.usecases.NotesManager
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
class NotesManagerService(
    private val jdbcTemplate: JdbcTemplate
) : NotesManager {

    private val rowMapper = RowMapper { rs, _ ->
        Note(
            uuid = UUID.fromString(rs.getString("uuid")),
            content = rs.getString("content"),
            createdAt = rs.getString("created_at").let { LocalDate.parse(it) }
        )
    }

    override fun addNote(note: Note, bonsaiUUID: UUID): Note {
        val sql = """
            INSERT INTO notes (uuid, content, bonsai_uuid)
            VALUES (?, ?, ?)
            RETURNING uuid
        """
        val newUuid: UUID? = jdbcTemplate.queryForObject(sql, UUID::class.java, UUID.randomUUID(), note.content, bonsaiUUID)

        return note.copy(uuid = newUuid)
    }

    override fun getAllNotesByBonsai(bonsaiUUID: UUID, page: Int, pageSize: Int): List<Note> {
        val offset = page.takeIf { page != 0 } ?: 1
        val sql = """
            SELECT uuid, content, created_at
            FROM notes
            WHERE bonsai_uuid = ?
            order by created_at desc
            limit ? offset (? - 1) * ?
        """
        return jdbcTemplate.query(sql, rowMapper, bonsaiUUID, pageSize, offset, pageSize)
    }

    override fun editNote(note: Note, bonsaiUUID: UUID): Note {
        val sql = """
            UPDATE notes
            SET content = ?
            WHERE uuid = ? AND bonsai_uuid = ?
        """

        jdbcTemplate.update(sql, note.content, note.uuid, bonsaiUUID)

        return note
    }

    override fun deleteNote(noteUuid: UUID) {
        val sql = "DELETE FROM notes WHERE uuid = ?"
        jdbcTemplate.update(sql, noteUuid)
    }
}