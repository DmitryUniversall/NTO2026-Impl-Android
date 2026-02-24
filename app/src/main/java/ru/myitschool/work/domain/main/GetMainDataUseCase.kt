package ru.myitschool.work.domain.main

import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.main.entities.MainInfoEntity
import java.time.LocalDate

class GetMainDataUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(): Result<MainInfoEntity> {
        return repository.getInfo().map { main ->
            main.copy(
                book = main.book.sortedBy { book ->
                    LocalDate.parse(book.date)
                }
            )
        }
    }
}