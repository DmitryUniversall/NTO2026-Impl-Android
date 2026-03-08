package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.domain.auth.entities.User

@Serializable
data class UserDTO(
    @SerialName("id")
    val id: Long?,

    @SerialName("login")
    val login: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("photoUrl")
    val photoUrl: String?
) {
    fun toEntity(): User = User(
        id = id ?: error("${this::class.simpleName}.id not specified"),
        name = name ?: error("${this::class.simpleName}.name not specified"),
        login = login ?: error("${this::class.simpleName}.login not specified"),
        photoUrl = photoUrl ?: error("${this::class.simpleName}.photoUrl not specified")
    )
}
