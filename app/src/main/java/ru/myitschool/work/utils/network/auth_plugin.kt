package ru.myitschool.work.utils.network

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey

val AuthTokenAttributeKey = AttributeKey<String>("AuthToken")
val SkipAuthAttributeKey = AttributeKey<Boolean>("SkipAuth")


class AuthPluginConfiguration {
    lateinit var tokenProvider: suspend () -> String?
}

val authPlugin = createClientPlugin(
    name = "AuthPlugin",
    createConfiguration = ::AuthPluginConfiguration
) {
    val config = pluginConfig

    onRequest { request, _  ->
        val skipAuth = request.attributes.getOrNull(SkipAuthAttributeKey) ?: false
        if (skipAuth) return@onRequest

        val token = request.attributes.getOrNull(AuthTokenAttributeKey) ?: config.tokenProvider()
        if (!token.isNullOrBlank()) {
            request.headers[HttpHeaders.Authorization] = "Basic $token"
        }
    }
}
