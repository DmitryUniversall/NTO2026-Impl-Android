package ru.myitschool.work.core.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalDate.toIsoString(): String {
    return this.format(DateTimeFormatter.ISO_LOCAL_DATE)
}

fun LocalTime.toHHMM(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}
