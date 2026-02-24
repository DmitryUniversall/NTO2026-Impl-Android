package ru.myitschool.work.ui.screen.main

sealed interface MainIntent {
    data object Refresh: MainIntent
    data object Logout: MainIntent
    data object Add: MainIntent
}