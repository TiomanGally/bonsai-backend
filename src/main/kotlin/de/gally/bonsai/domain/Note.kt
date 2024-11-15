package de.gally.bonsai.domain

import java.time.LocalDate
import java.util.UUID

data class Note(
    val uuid: UUID? = null,
    val content: String,
    val createdAt: LocalDate = LocalDate.now()
)