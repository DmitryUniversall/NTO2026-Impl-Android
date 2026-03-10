package ru.myitschool.work.ui.screen.main.employee

import ru.myitschool.work.ui.nav.AppDestination

sealed interface EmployeeMainAction {
    class Navigate(
        val destination: AppDestination,
        val clearBackStack: Boolean = false
    ) : EmployeeMainAction
}
