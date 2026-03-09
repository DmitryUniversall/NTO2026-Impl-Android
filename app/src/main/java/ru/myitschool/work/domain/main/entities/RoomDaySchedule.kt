package ru.myitschool.work.domain.main.entities

import java.time.LocalDateTime

data class RoomDaySchedule(  // TODO
    val isBooked: Boolean,
    val bookedBy: String?,
    val bookedAt: LocalDateTime?
) {
    companion object {
        fun free(): RoomDaySchedule = RoomDaySchedule(
            isBooked = false,
            bookedBy = null,
            bookedAt = null
        )
    }
}
