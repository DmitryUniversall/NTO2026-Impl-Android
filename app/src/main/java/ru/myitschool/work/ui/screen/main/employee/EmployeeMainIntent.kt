package ru.myitschool.work.ui.screen.main.employee

sealed interface EmployeeMainIntent {
    data object Refresh: EmployeeMainIntent
    data object Logout: EmployeeMainIntent
    data object Add: EmployeeMainIntent
}