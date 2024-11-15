package de.gally.bonsai.adapter

import de.gally.bonsai.domain.User
import de.gally.bonsai.domain.usecases.UserService
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserDatabaseService(
    private val userRepository: UserRepository
) : UserService {

    override fun addUser(name: String, email: String): User {
        return userRepository.save(DbUser(name = name, email = email.lowercase())).toDomain()
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email.lowercase())?.toDomain()
    }

    private fun DbUser.toDomain(): User = User(uuid!!, name, email)
}

@Table("users")
data class DbUser(
    @Id val uuid: UUID? = null,
    val name: String,
    val email: String
)

@Repository
interface UserRepository : CrudRepository<DbUser, UUID> {
    fun findByEmail(email: String): DbUser?
}