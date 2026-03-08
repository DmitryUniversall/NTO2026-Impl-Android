package ru.myitschool.work.data.repo

import ru.myitschool.work.data.dto.book.BookRequestDTO
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.book.entities.BookRequestData
import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.book.entities.UserBookingData

object BookRepository {
    suspend fun getUserBookings(): Result<List<UserBookingData>> {
        return NetworkDataSource.getUserBookings().mapCatching { dto ->
            dto?.map { (date, placeDto) ->
                UserBookingData(
                    date = date,
                    place = placeDto.toEntity()
                )
            } ?: error("map is null")
        }
    }

    suspend fun getDailyBookingInfo(): Result<List<BookingData>> {
        return NetworkDataSource.getAvailablePlaces().mapCatching { dto ->
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

    suspend fun book(data: BookRequestData): Result<Boolean> {
        val dto = BookRequestDTO(data.date, data.placeId)
        return NetworkDataSource.book(dto)
    }
}
