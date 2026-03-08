package ru.myitschool.work.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.LoginUseCase
import ru.myitschool.work.ui.nav.MainScreenDestination

class AuthViewModel : ViewModel() {
    private val loginUseCase by lazy { LoginUseCase(AuthRepository) }
    private val _uiState = MutableStateFlow<AuthState>(
        AuthState.Data(
            isEnabledSend = false,
            error = null
        )
    )
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _actionFlow: MutableSharedFlow<AuthAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<AuthAction> = _actionFlow

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SendLogin -> {
                viewModelScope.launch {
                    loginUseCase.invoke(intent.login, intent.password).fold(
                        onSuccess = {
                            _actionFlow.emit(AuthAction.Navigate(MainScreenDestination))
                        },
                        onFailure = { error ->
                            updateStateIfData { oldState ->
                                oldState.copy(
                                    error = error.message
                                )
                            }
                        }
                    )
                }
            }

            is AuthIntent.LoginInput -> {
                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = intent.text.isNotEmpty(),
                        error = null
                    )
                }
            }

            is AuthIntent.PasswordInput -> {
                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = intent.text.isNotEmpty(),
                        error = null
                    )
                }
            }
        }
    }

    private fun updateStateIfData(lambda: (AuthState.Data) -> AuthState) {
        _uiState.update { state ->
            (state as? AuthState.Data)?.let { lambda.invoke(it) } ?: state
        }
    }
}
