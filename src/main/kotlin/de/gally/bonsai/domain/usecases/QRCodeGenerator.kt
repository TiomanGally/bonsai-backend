package de.gally.bonsai.domain.usecases

import java.io.File
import java.util.UUID

fun interface QRCodeGenerator {
    operator fun invoke(name: String, uuid: UUID, text: String): File
}