package ru.myitschool.work.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.data.dto.UserDTO

@Serializable
data class LoginResponseDTO(
    @SerialName("user")
    val user: UserDTO?
)
