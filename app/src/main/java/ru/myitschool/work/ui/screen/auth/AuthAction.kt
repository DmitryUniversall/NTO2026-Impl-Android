package ru.myitschool.work.ui.screen.auth

import ru.myitschool.work.ui.nav.AppDestination

sealed interface AuthAction {
    class Navigate(val destination: AppDestination): AuthAction
}
