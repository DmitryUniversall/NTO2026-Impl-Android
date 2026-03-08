package ru.myitschool.work.ui.screen.auth

sealed interface AuthIntent {
    data class SendLogin(val login: String, val password: String): AuthIntent
    data class LoginInput(val text: String): AuthIntent
    data class PasswordInput(val text: String): AuthIntent
}
