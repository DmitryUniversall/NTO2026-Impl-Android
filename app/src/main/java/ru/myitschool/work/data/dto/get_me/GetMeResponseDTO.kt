package ru.myitschool.work.data.dto.get_me

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.data.dto.UserDTO

@Serializable
data class GetMeResponseDTO(
    @SerialName("user")
    val user: UserDTO? = null
)
