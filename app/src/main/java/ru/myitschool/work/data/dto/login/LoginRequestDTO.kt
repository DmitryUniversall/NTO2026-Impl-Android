package ru.myitschool.work.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    @SerialName("login")
    val login: String,

    @SerialName("password")
    val password: String
)
