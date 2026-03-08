package ru.myitschool.work.ui.screen.init

sealed interface InitState {
    object Idle : InitState
    object Loading : InitState
    object Success : InitState
    data class Error(val errorMessage: String) : InitState
}
