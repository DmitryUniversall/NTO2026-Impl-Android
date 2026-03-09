package ru.myitschool.work.data.repo

import ru.myitschool.work.core.utils.toIsoString
import ru.myitschool.work.data.dto.book.BookRequestDTO
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.book.entities.BookRequestData
import ru.myitschool.work.domain.book.entities.BookingData
import ru.myitschool.work.domain.book.entities.Place
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import java.time.LocalDate

object BookRepository {
    suspend fun getDailyBookingInfo(): Result<List<BookingData>> {
        return NetworkDataSource.getAvailablePlaces().mapCatching { dto ->
            dto?.map { (date, places) ->
                BookingData(
                    date = date,
                    places = places.mapNotNull { place ->
                        Place(
                            id = place.id ?: return@mapNotNull null,
                            name = place.place ?: return@mapNotNull null
                        )
                    }
                )
            } ?: error("map is null")
        }
    }

    suspend fun getRoomSchedule(): Result<Map<LocalDate, RoomDaySchedule>> {
        return NetworkDataSource.getRoomSchedule().map { dto ->
            val result = mutableMapOf<LocalDate, RoomDaySchedule>()

            val now = LocalDate.now()

            for (i in 0L..2L) {
                val date = now.plusDays(i)
                result[date] = dto.getOrDefault(date.toIsoString(), null)?.toEntity() ?: RoomDaySchedule.free()
            }

            result
        }
    }

    suspend fun book(data: BookRequestData?): Result<Boolean> {
        return NetworkDataSource.book(data?.let { BookRequestDTO(it.date, it.placeId) })
    }

    suspend fun cancelBooking(): Result<Boolean> {
        return NetworkDataSource.cancelCurrentRoomBooking()
    }
}
