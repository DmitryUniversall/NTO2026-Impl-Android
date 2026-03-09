package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.domain.book.entities.Place

@Serializable
data class PlaceDTO(
    @SerialName("id")
    val id: String? = null,
    @SerialName("place")
    val place: String? = null
) {
    fun toEntity() = Place(
        id = id ?: error("${this::class.simpleName}.id not specified"),
        name = place ?: error("${this::class.simpleName}.place not specified")
    )
}
