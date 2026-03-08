package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository

class GetAuthTokenUseCase(
    val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return authRepository.getAuthToken()
    }
}
