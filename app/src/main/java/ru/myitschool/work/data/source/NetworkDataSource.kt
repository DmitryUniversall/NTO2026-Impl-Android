package ru.myitschool.work.data.source

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.myitschool.work.core.Constants
import ru.myitschool.work.data.dto.PlaceDTO
import ru.myitschool.work.data.dto.RoomDayScheduleDTO
import ru.myitschool.work.data.dto.book.BookRequestDTO
import ru.myitschool.work.data.dto.login.LoginResponseDTO
import ru.myitschool.work.utils.network.AppHttpProvider
import ru.myitschool.work.utils.network.AuthTokenAttributeKey
import ru.myitschool.work.utils.network.getOrError

object NetworkDataSource {
    private val client = AppHttpProvider.client

    private fun getUrl(targetUrl: String) = "${Constants.HOST}/api/$targetUrl"

    suspend fun login(token: String): Result<LoginResponseDTO> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.LOGIN_URL)) {
                attributes.put(AuthTokenAttributeKey, token)
            }

            response.getOrError()
        }
    }

    suspend fun reLogin(): Result<LoginResponseDTO> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.LOGIN_URL))
            response.getOrError()
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

    suspend fun getAvailablePlaces(): Result<Map<String, List<PlaceDTO>>?> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.GET_AVAILABLE_PLACES_URL))
            response.getOrError()
        }
    }

    suspend fun getRoomSchedule(): Result<Map<String, RoomDayScheduleDTO>> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.get(getUrl(Constants.GET_ROOM_SCHEDULE_URL))
            response.getOrError()
        }
    }

    suspend fun cancelCurrentRoomBooking(): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = client.delete(getUrl(Constants.CANCEL_ROOM_BOOKING_URL))

            when (response.status) {
                HttpStatusCode.NoContent -> true
                else -> error(response.bodyAsText())
            }
        }
    }
}
