package de.gally.bonsai.port.rest.controller

import de.gally.bonsai.config.BonsaiUser
import de.gally.bonsai.domain.User
import de.gally.bonsai.domain.usecases.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserV1Controller(
    private val userService: UserService
) {

    /**
     * Finds the user but if the user does not exist it will persist the user.
     * So you think the database will grow very fast as each user will be persisted?
     * No! As this application is only for my home and a lot of people don't live there.
     */
    @GetMapping
    fun getUserInformation(
        authentication: Authentication
    ): User {
        val bonsaiUser = authentication as BonsaiUser
        return userService.findUserByEmail(bonsaiUser.email)
            ?: userService.addUser(bonsaiUser.fullName, bonsaiUser.email)
    }
}
