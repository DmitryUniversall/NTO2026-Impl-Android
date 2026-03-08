package ru.myitschool.work.ui.screen.main

import ru.myitschool.work.ui.nav.AppDestination

sealed interface MainAction {
    class Navigate(
        val destination: AppDestination,
        val clearBackStack: Boolean = false
    ): MainAction
}