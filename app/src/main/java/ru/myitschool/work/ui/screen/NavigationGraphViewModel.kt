package ru.myitschool.work.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.GetAuthFlowUseCase
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.ui.nav.AuthScreenDestination

class NavigationGraphViewModel : ViewModel() {
    private val getAthFlowUseCase = GetAuthFlowUseCase(AuthRepository)

    private val _actions = MutableSharedFlow<NavigationGraphAction>()
    val actions = _actions.asSharedFlow()

    init {
        subscribeToAuthState()
    }

    fun subscribeToAuthState() {
        var previousAuthState: AuthState? = null

        viewModelScope.launch {
            getAthFlowUseCase()
                .collect { state ->
                    when (state) {
                        is AuthState.Unauthenticated -> {
                            Log.d("Auth", "Auth state is now AuthState.Unauthenticated")

                            if (previousAuthState !is AuthState.Authenticated) {
                                _actions.emit(NavigationGraphAction.Navigate(AuthScreenDestination))
                            } else {
                                _actions.emit(NavigationGraphAction.ShowDialog(NavigationGraphDialog.AuthRequired))
                            }
                        }

                        is AuthState.Authenticated -> Log.d("Auth", "Auth state is now AuthState.Authenticated")
                        is AuthState.Unknown -> {}
                    }

                    previousAuthState = state
                }
        }
    }
}