package ru.myitschool.work.ui.screen.init

import ru.myitschool.work.ui.nav.AppDestination

sealed interface InitAction {
    data class Navigate(val destination: AppDestination) : InitAction
}
