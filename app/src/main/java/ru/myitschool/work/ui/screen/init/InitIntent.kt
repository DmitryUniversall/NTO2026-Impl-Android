package ru.myitschool.work.ui.screen.init

sealed interface InitIntent {
    object Refresh : InitIntent
}
