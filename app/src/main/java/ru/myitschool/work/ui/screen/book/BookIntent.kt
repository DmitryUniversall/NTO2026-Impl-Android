package ru.myitschool.work.ui.screen.book

sealed interface BookIntent {
    data object Refresh: BookIntent
    data class Add(
        val date: String,
        val placeId: String
    ): BookIntent
}
