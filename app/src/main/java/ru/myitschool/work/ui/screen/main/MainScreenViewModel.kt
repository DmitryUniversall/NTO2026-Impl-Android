package ru.myitschool.work.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.GetAuthFlowUseCase
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.domain.auth.entities.UserRole
import ru.myitschool.work.ui.screen.MainScreenIntent

class MainScreenViewModel : ViewModel() {
    private val getAuthFlowUseCase = GetAuthFlowUseCase(AuthRepository)

    private val _uiState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            checkUserRoles()
        }
    }

    private suspend fun checkUserRoles() {
        _uiState.value = MainScreenState.Loading

        _uiState.value = when (val authState = getAuthFlowUseCase().first()) {
            is AuthState.Authenticated -> when (authState.user.userRole) {
                UserRole.USER -> MainScreenState.Employee
                UserRole.MEETING_ROOM -> MainScreenState.Device
            }

            is AuthState.Unauthenticated -> MainScreenState.Error("Unauthenticated")
            is AuthState.Unknown -> MainScreenState.Error("Unauthenticated (Unknown)")
        }
    }

    fun onIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.Refresh -> viewModelScope.launch { checkUserRoles() }
        }
    }
}
