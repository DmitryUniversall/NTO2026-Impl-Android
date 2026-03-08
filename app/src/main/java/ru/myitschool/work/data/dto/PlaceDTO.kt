package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.domain.book.entities.BookingData

@Serializable
data class PlaceDTO(
    @SerialName("id")
    val id: String?,
    @SerialName("place")
    val place: String?
) {
    fun toEntity() = BookingData.Place(
        id = id ?: error("${this::class.simpleName}.id not specified"),
        name = place ?: error("${this::class.simpleName}.place not specified")
    )
}
