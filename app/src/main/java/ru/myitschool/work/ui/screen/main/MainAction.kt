package ru.myitschool.work.ui.screen.main

import ru.myitschool.work.ui.nav.AppDestination
import ru.myitschool.work.ui.screen.book.BookIntent

sealed interface MainAction {
    class Open(
        val destination: AppDestination,
        val clearBackStack: Boolean = false
    ): MainAction
}