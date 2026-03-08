package ru.myitschool.work.data.source

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.myitschool.work.core.Constants
import ru.myitschool.work.data.dto.PlaceDTO
import ru.myitschool.work.data.dto.book.BookRequestDTO
import ru.myitschool.work.data.dto.get_me.GetMeResponseDTO
import ru.myitschool.work.data.dto.login.LoginRequestDTO
import ru.myitschool.work.data.dto.login.LoginResponseDTO
import ru.myitschool.work.domain.auth.exceptions.UnauthenticatedException
import ru.myitschool.work.domain.common.exceptions.ApiRequestFailedException
import ru.myitschool.work.utils.network.AuthTokenAttributeKey
import ru.myitschool.work.utils.network.AppHttpProvider
import ru.myitschool.work.utils.network.SkipAuthAttributeKey
import ru.myitschool.work.utils.network.getOrError

object NetworkDataSource {
    private val client = AppHttpProvider.client

    private fun getUrl(targetUrl: String) = "${Constants.HOST}/api/$targetUrl"

    suspend fun getMeUsingToken(token: String): Result<GetMeResponseDTO> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.GET_ME_URL)) {
                attributes.put(AuthTokenAttributeKey, token)
            }

            when (response.status) {
                HttpStatusCode.OK -> response.getOrError<GetMeResponseDTO>()
                HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> throw UnauthenticatedException("Failed to get user data")
                else -> throw ApiRequestFailedException("Failed to get user data", response)
            }
        }
    }

    suspend fun getMe(): Result<GetMeResponseDTO> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.GET_ME_URL))
            response.getOrError<GetMeResponseDTO>()
        }
    }

    suspend fun login(dto: LoginRequestDTO): Result<LoginResponseDTO> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.post(getUrl(Constants.LOGIN_URL)) {
                attributes.put(SkipAuthAttributeKey, true)
                setBody(dto)
            }

            response.getOrError<LoginResponseDTO>()
        }
    }

    suspend fun book(data: BookRequestDTO): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.post(getUrl(Constants.BOOK_URL)) {
                setBody(data)
            }

            when (response.status) {
                HttpStatusCode.Created -> true
                HttpStatusCode.Conflict -> error(response.bodyAsText())
                else -> error(response.bodyAsText())
            }
        }
    }

    suspend fun getUserBookings(): Result<Map<String, PlaceDTO>?> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.GET_USER_BOOKINGS_URL))
            response.getOrError<Map<String, PlaceDTO>?>()
        }
    }

    suspend fun getAvailablePlaces(): Result<Map<String, List<PlaceDTO>>?> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.GET_AVAILABLE_PLACES_URL))
            response.getOrError<Map<String, List<PlaceDTO>>?>()
        }
    }
}
