package ru.myitschool.work.domain.main.entities

import java.time.LocalDateTime

sealed interface RoomDaySchedule {
    object Free : RoomDaySchedule

    data class Bookend(
        val bookedBy: String,
        val bookedAt: LocalDateTime
    ) : RoomDaySchedule
}

val RoomDaySchedule.isBooked: Boolean get() = this is RoomDaySchedule.Bookend
