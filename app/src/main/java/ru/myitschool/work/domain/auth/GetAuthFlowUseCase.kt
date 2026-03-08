package ru.myitschool.work.domain.auth

import kotlinx.coroutines.flow.StateFlow
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.entities.AuthState

class GetAuthFlowUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<AuthState> = authRepository.authState
}
