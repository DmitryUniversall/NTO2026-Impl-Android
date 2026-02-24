package ru.myitschool.work.data.repo

import ru.myitschool.work.data.dto.BookRequestDto
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.book.entities.BookRequestData
import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.main.entities.MainInfoEntity

class BookRepository(
    private val authRepository: AuthRepository
) {
    suspend fun getInfo(): Result<MainInfoEntity> {
        val code = authRepository.getCode() ?: return getNoAuthResult()
        return NetworkDataSource.getInfo(code).mapCatching { dto ->
            MainInfoEntity(
                name = dto.name ?: error("Name is null"),
                photoUrl = dto.photoUrl ?: error("Photo url is null"),
                book = dto.booking?.mapNotNull { (date, place) ->
                    MainInfoEntity.Book(
                        date = date,
                        place = place.place ?: return@mapNotNull null
                    )
                } ?: listOf()
            )
        }
    }

    suspend fun getBookingInfo(): Result<List<BookingData>> {
        val code = authRepository.getCode() ?: return getNoAuthResult()
        return NetworkDataSource.getBooking(code).mapCatching { dto ->
            dto?.map { (date, places) ->
                BookingData(
                    date = date,
                    places = places.mapNotNull { place ->
                        BookingData.Place(
                            id = place.id ?: return@mapNotNull null,
                            name = place.place ?: return@mapNotNull null
                        )
                    }
                )
            } ?: error("map is null")
        }
    }

    suspend fun sendBook(data: BookRequestData): Result<Boolean> {
        val code = authRepository.getCode() ?: return getNoAuthResult()
        val dto = BookRequestDto(data.date, data.placeId)
        return NetworkDataSource.addBook(code, dto)
    }
    private fun <T> getNoAuthResult() = Result.failure<T>(
        IllegalStateException("No auth")
    )
}