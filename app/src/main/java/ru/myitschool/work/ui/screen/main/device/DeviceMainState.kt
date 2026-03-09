package ru.myitschool.work.ui.screen.main.device

import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import java.time.LocalDate

data class DeviceMainState(
    val selectedDate: LocalDate,
    val selectedDaySchedule: ResourceState<RoomDaySchedule>,
    val bookRequest: ResourceState<Unit>
) {
    companion object {
        fun empty(): DeviceMainState = DeviceMainState(
            selectedDate = LocalDate.now(),
            selectedDaySchedule = ResourceState.Idle,
            bookRequest = ResourceState.Idle
        )
    }
}
