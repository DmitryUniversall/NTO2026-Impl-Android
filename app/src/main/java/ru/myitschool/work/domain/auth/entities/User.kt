package ru.myitschool.work.domain.auth.entities

data class User(
    val id: Long,
    val name: String,
    val login: String,
    val photoUrl: String,
    val userRole: UserRole
)
