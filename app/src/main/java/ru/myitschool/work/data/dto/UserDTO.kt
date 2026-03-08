package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.domain.auth.entities.User
import ru.myitschool.work.domain.auth.entities.UserRole

@Serializable
data class UserDTO(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("login")
    val login: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("photoUrl")
    val photoUrl: String? = null,

    @SerialName("userRole")
    val userRole: String? = null
) {
    fun toEntity(): User = User(
        id = id ?: error("${this::class.simpleName}.id not specified"),
        name = name ?: error("${this::class.simpleName}.name not specified"),
        login = login ?: error("${this::class.simpleName}.login not specified"),
        photoUrl = photoUrl ?: error("${this::class.simpleName}.photoUrl not specified"),
        userRole = userRole?.let { UserRole.valueOf(it) } ?: error("${this::class.simpleName}.userRole not specified")
    )
}
