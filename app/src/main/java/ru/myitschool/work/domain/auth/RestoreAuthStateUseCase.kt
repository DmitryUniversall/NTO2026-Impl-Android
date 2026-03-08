package ru.myitschool.work.domain.auth

import android.util.Log
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.entities.AuthState

class RestoreAuthStateUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<AuthState> {
        return runCatching {
            authRepository.restoreAuthState()
        }.onFailure { error ->
            Log.w("Auth", "Failed to load local auth", error)
        }
    }
}
