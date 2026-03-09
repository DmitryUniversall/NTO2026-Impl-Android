package ru.myitschool.work.ui.screen.main.device

import java.time.LocalDate

sealed interface DeviceMainIntent {
    object Logout : DeviceMainIntent
    object Refresh : DeviceMainIntent
    object BookForToday : DeviceMainIntent
    data class SelectDate(val date: LocalDate) : DeviceMainIntent
}
