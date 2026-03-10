package ru.myitschool.work.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import java.time.LocalDateTime

@Serializable
data class RoomDayScheduleDTO(
    @SerialName("userName")
    val userName: String? = null,

//    @SerialName("bookedAt")
//    val bookedAt: String? = null
) {
    fun toEntity(): RoomDaySchedule.Bookend = RoomDaySchedule.Bookend(
        bookedBy = userName ?: error("${this::class.simpleName}.userName not specified"),
        bookedAt = LocalDateTime.now()  // LocalDateTime.parse(bookedAt) ?: error("${this::class.simpleName}.bookedAt not specified")
    )
}
