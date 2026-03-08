package ru.myitschool.work.data.dto.book

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookRequestDTO(
    @SerialName("date")
    val date: String,
    @SerialName("placeId")
    val placeId: String
)
