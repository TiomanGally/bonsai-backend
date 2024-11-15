package de.gally.bonsai.domain.usecases

import de.gally.bonsai.domain.User

interface UserService {

    fun addUser(name: String, email: String): User

    fun findUserByEmail(email: String): User?
}