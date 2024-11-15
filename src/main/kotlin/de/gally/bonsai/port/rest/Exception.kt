package de.gally.bonsai.port.rest

import java.util.UUID

sealed class NotFoundException(type: String, id: Any) : RuntimeException("$type not found with uuid $id")
class BonsaiNotFoundException(uuid: UUID) : NotFoundException("Bonsai", uuid)
class PictureNotFoundException(uuid: UUID) : NotFoundException("Picture", uuid)
class UserNotFoundException(email: String) : NotFoundException("User", email)

class UnsupportedFileTypeException(message: String) : RuntimeException(message)
