package de.gally.bonsai.domain

import java.util.UUID

data class User(
    val uuid: UUID,
    val name: String,
    val email: String,
)
