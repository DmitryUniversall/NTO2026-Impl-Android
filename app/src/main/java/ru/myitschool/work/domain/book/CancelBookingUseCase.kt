package ru.myitschool.work.domain.book

import ru.myitschool.work.data.repo.BookRepository

class CancelBookingUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): Result<Boolean> = bookRepository.cancelBooking()
}
