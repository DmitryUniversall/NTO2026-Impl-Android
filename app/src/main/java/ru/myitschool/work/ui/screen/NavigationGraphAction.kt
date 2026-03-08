package ru.myitschool.work.ui.screen

import ru.myitschool.work.ui.nav.AppDestination

interface NavigationGraphAction {
    data class Navigate(val destination: AppDestination, val clearBackStack: Boolean = true) : NavigationGraphAction

    data class ShowDialog(val dialogType: NavigationGraphDialog) : NavigationGraphAction
}
