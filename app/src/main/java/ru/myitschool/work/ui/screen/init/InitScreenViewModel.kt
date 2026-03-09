package ru.myitschool.work.ui.screen.init

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.LogoutUseCase
import ru.myitschool.work.domain.auth.RestoreAuthStateUseCase
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.MainScreenDestination

class InitScreenViewModel : ViewModel() {
    private val restoreAuthStateUseCase by lazy { RestoreAuthStateUseCase(AuthRepository) }
    private val logoutUseCase by lazy { LogoutUseCase(AuthRepository) }

    private val _uiState = MutableStateFlow<InitState>(InitState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<InitAction>()
    val actions = _actions.asSharedFlow()

    init {
        launchTryToRestoreAuthState()
    }

    private suspend fun tryToRestoreAuthState() {
        if (_uiState.firstOrNull() is InitState.Loading) {
            Log.w("InitScreen", "Trying to call tryToRestoreAuthState when already loading")
            return
        }

        _uiState.emit(InitState.Loading)

        _uiState.update {
            restoreAuthStateUseCase.invoke().fold(
                onSuccess = { state ->
                    when (state) {
                        is AuthState.Authenticated -> {
                            InitState.Success.also {
                                _actions.emit(InitAction.Navigate(MainScreenDestination))
                            }
                        }

                        is AuthState.Unauthenticated -> {
                            InitState.Idle.also {
                                _actions.emit(InitAction.Navigate(AuthScreenDestination))
                            }
                        }

                        is AuthState.Unknown -> {
                            InitState.Error("Failed to load local auth").also {
                                _actions.emit(InitAction.Navigate(AuthScreenDestination))
                            }
                        }
                    }
                },
                onFailure = { error ->
                    Log.d("Auth", "Error class: ${error::class.simpleName}", error)
                    InitState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    private fun launchTryToRestoreAuthState() {
        viewModelScope.launch {
            tryToRestoreAuthState()
        }
    }

    private fun launchLogout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun onIntent(intent: InitIntent) {
        when (intent) {
            is InitIntent.Refresh -> launchTryToRestoreAuthState()
            is InitIntent.Logout -> launchLogout()
        }
    }
}
