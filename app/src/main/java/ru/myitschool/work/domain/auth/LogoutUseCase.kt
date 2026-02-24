package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}