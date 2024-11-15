package de.gally.bonsai.domain.usecases

import de.gally.bonsai.domain.Picture
import java.util.UUID

interface PictureManager {

    fun addPicture(bonsaiUuid: UUID, fileName: String, picture: ByteArray): Picture

    fun deletePicture(bonsaiUuid: UUID, pictureUuid: UUID)

    fun getAllPictureUuids(bonsaiUuid: UUID, page: Int, pageSize: Int): List<UUID>

    fun getPictureByUuid(bonsaiUuid: UUID, pictureUuid: UUID): Picture?
}