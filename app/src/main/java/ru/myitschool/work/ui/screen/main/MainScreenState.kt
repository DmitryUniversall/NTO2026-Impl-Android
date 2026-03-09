package ru.myitschool.work.ui.screen.main

sealed interface MainScreenState {
    object Loading : MainScreenState
    data class Error(val message: String) : MainScreenState

    object Employee : MainScreenState
    object Device : MainScreenState
}
