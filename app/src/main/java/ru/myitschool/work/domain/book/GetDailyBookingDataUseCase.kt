package ru.myitschool.work.domain.book

import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.book.entities.BookingData
import java.time.LocalDate

class GetDailyBookingDataUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(): Result<List<BookingData>> {
        return repository.getDailyBookingInfo().map { data ->
            data
                .sortedBy { book ->
                    LocalDate.parse(book.date)
                }
                .filter { it.places.isNotEmpty() }
        }
    }
}
