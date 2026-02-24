package ru.myitschool.work.ui.screen.book

sealed interface BookAction {
    object Back: BookAction
    object BackWithSuccess: BookAction
}