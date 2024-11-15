package de.gally.bonsai.domain

import java.time.LocalDate
import java.util.UUID

data class Bonsai(
    val uuid: UUID = UUID.randomUUID(),
    val latinName: String,
    val simpleName: String?,
    val birthDate: LocalDate,
    val price: Double,
    val lastRepoted: LocalDate? = null
)
