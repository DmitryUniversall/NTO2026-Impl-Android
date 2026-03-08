package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.entities.AuthState

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ): Result<AuthState.Authenticated> {
        return repository.login(login, password)
    }
}
