package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            authRepository.logout()
        }
    }
}
