package ru.myitschool.work.ui.screen.main.device

import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.core.ui.state.isFetching
import ru.myitschool.work.domain.auth.entities.User
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import java.time.LocalDate
import java.time.LocalDateTime

data class DeviceMainState(
    val currentDateTime: LocalDateTime,
    val selectedDate: LocalDate,
    val schedule: ResourceState<Map<LocalDate, RoomDaySchedule>>,
    val bookRequest: ResourceState<Unit>,
    val cancelBookingRequest: ResourceState<Unit>,
    val me: ResourceState<User>,
) {
    val isFetching get() = schedule.isFetching

    companion object {
        fun empty(): DeviceMainState = DeviceMainState(
            currentDateTime = LocalDateTime.now(),
            selectedDate = LocalDate.now(),
            schedule = ResourceState.Idle,
            bookRequest = ResourceState.Idle,
            cancelBookingRequest = ResourceState.Idle,
            me = ResourceState.Idle
        )
    }
}
