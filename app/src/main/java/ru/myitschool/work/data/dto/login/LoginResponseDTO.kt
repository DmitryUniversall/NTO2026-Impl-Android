package ru.myitschool.work.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.data.dto.PlaceDTO
import ru.myitschool.work.domain.auth.entities.User
import ru.myitschool.work.domain.auth.entities.UserRole
import java.time.LocalDate

@Serializable
data class LoginResponseDTO(
    @SerialName("name")
    val name: String? = null,

    @SerialName("photoUrl")
    val photoUrl: String? = null,

    @SerialName("role")
    val role: String? = null,

    @SerialName("booking")
    val booking: Map<String, PlaceDTO?>?
) {
    fun toUser(): User = User(
        name = name ?: error("${this::class.simpleName}.name not specified"),
        photoUrl = photoUrl,
        userRole = role?.let { UserRole.valueOf(it) } ?: error("${this::class.simpleName}.userRole not specified"),
        booking = booking?.map { (date, placeDTO) ->
            LocalDate.parse(date) to (placeDTO?.toEntity() ?: error("${this::class.simpleName}.booking[${date}] not specified"))
        }?.toMap() ?: mapOf()
    )
}
