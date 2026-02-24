package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookRequestDto(
    @SerialName("date")
    val date: String,
    @SerialName("placeId")
    val placeId: String,
)