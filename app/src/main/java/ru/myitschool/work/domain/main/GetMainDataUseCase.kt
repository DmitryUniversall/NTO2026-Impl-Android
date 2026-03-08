package ru.myitschool.work.domain.main

import kotlinx.coroutines.flow.firstOrNull
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.domain.main.entities.MainInfoEntity
import java.time.LocalDate

class GetMainDataUseCase(
    private val authRepository: AuthRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): Result<MainInfoEntity> {
        return runCatching {
            val authInfo = authRepository.authState.firstOrNull()
            if (authInfo !is AuthState.Authenticated) error("Unauthenticated")

            val bookings = bookRepository.getUserBookings().getOrThrow()

            MainInfoEntity(
                name = authInfo.user.name,
                photoUrl = authInfo.user.photoUrl,
                book = bookings
                    .map { booking ->
                        MainInfoEntity.Book(
                            date = booking.date,
                            place = booking.place.name
                        )
                    }
                    .sortedBy { book ->
                        LocalDate.parse(book.date)
                    }
            )
        }
    }
}