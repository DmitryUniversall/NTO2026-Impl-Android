package ru.myitschool.work.domain.auth.entities

sealed class AuthState {
    object Unknown : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(
        val basicToken: String,
        val user: User,
//        val accessInfo: AccessInfo
    ) : AuthState()
}
