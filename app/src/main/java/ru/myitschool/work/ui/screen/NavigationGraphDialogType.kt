package ru.myitschool.work.ui.screen

sealed interface NavigationGraphDialog {
    object AuthRequired : NavigationGraphDialog
    data class Message(val title: String, val message: String) : NavigationGraphDialog
}
