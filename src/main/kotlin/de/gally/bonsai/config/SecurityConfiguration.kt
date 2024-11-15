package de.gally.bonsai.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource


@Configuration
class SecurityConfiguration {

    @Bean
    @Order(1)
    fun general(
        httpSecurity: HttpSecurity,
        @Value("\${bonsai.ui.link}") bonsaiUi: String,
        @Value("\${spring.security.oauth2.client.registration.bonsai.client-id}") clientId: String,
        @Value("\${bonsai.backend.link}") bonsaiBackend: String,
    ): SecurityFilterChain {
        httpSecurity {
            cors {
                configurationSource = CorsConfigurationSource {
                    CorsConfiguration().apply {
                        allowedOrigins = listOf(bonsaiUi, bonsaiBackend)
                        allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        allowedHeaders = listOf(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
                        allowCredentials = true
                    }
                }
            }
            // We will make sure that every request has to have an authorization header containing a bearer token.
            httpBasic { disable() }
            // Every Request has to be authenticated
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            // Don't save any sessions. This Backend is a Resource Server. Every Resource is secured.
            // Every Request is stateless. Besides that it's recommended to set this value as
            // we don't cache any session and validate the token everytime.
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = BonsaiJwtConverter(clientId)
                }
            }
        }
        return httpSecurity.build()
    }

    internal class BonsaiJwtConverter(
        private val clientId: String
    ) : Converter<Jwt, AbstractAuthenticationToken> {
        override fun convert(jwt: Jwt): AbstractAuthenticationToken {
            val email = jwt.getClaim<String>("email")
            val name = jwt.getClaim<String>("name")
            val roles = jwt.getClaim<Map<String, Map<String, List<String>>>>("resource_access")[clientId]
                ?.get("roles")
                ?.map { GrantedAuthority { it } }
                ?: emptyList()
            return BonsaiUser(jwt, email, name, roles)
        }
    }
}

class BonsaiUser(
    private val jwt: Jwt,
    val email: String,
    val fullName: String,
    private val authorities: Collection<GrantedAuthority>,
) : AbstractOAuth2TokenAuthenticationToken<Jwt>(jwt, email, jwt, authorities) {
    override fun getTokenAttributes(): MutableMap<String, Any> = jwt.claims

    override fun isAuthenticated(): Boolean = authorities.isNotEmpty()
}
