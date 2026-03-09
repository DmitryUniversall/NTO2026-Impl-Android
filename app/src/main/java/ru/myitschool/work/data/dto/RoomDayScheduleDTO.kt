package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomDayScheduleDTO(
    @SerialName("employeeName")
    val employeeName: String
)
