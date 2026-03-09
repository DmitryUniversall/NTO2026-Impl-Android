package ru.myitschool.work.domain.main

import kotlinx.coroutines.flow.first
import ru.myitschool.work.core.utils.toIsoString
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.domain.main.entities.MainInfoEntity
import java.time.LocalDate

class GetMainDataUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(fetch: Boolean = false): Result<MainInfoEntity> {
        return runCatching {
            val authState: AuthState
            if (fetch) {
                authState = authRepository.reLogin().getOrThrow()
            } else {
                authState = authRepository.authState.first()
                if (authState !is AuthState.Authenticated) error("Unauthenticated")
            }

            MainInfoEntity(
                name = authState.user.name,
                photoUrl = authState.user.photoUrl!!,
                book = authState.user.booking
                    .map { (date, place) ->
                        MainInfoEntity.Book(
                            date = date.toIsoString(),
                            place = place.name
                        )
                    }
                    .sortedBy { book ->
                        LocalDate.parse(book.date)
                    }
            )
        }
    }
}