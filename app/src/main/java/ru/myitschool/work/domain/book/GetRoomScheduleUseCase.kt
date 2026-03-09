package ru.myitschool.work.domain.book

import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import java.time.LocalDate

class GetRoomScheduleUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): Result<Map<LocalDate, RoomDaySchedule>> {
        return bookRepository.getRoomSchedule()
    }
}
