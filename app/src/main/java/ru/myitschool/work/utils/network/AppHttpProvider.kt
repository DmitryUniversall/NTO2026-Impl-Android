package ru.myitschool.work.utils.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import ru.myitschool.work.core.jsonCore
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.GetAuthTokenUseCase

object AppHttpProvider {
    val client by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(jsonCore)
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            install(authPlugin) {
                tokenProvider = GetAuthTokenUseCase(AuthRepository)::invoke
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}
