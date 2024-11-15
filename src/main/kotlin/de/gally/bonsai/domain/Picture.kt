package de.gally.bonsai.domain

import java.time.LocalDate
import java.util.UUID

class Picture(
    val uuid: UUID,
    val fileName: String,
    val content: ByteArray,
    val createdAt: LocalDate = LocalDate.now()
)
