package ru.myitschool.work.domain.book

import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.book.entities.BookRequestData

class SendBookRequestUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(data: BookRequestData?): Result<Unit> {
        return repository.book(data).mapCatching { success ->
            if (!success) error("Book error")
        }
    }
}
