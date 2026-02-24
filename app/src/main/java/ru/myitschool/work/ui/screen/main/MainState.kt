package ru.myitschool.work.ui.screen.main

import kotlinx.collections.immutable.PersistentList

sealed interface MainState {
    data object Loading: MainState
    data class Error(
        val error: String
    ): MainState
    data class Data(
        val name: String,
        val photoUrl: String,
        val books: PersistentList<Book>
    ): MainState {
        data class Book(
            val date: String,
            val place: String,
        )
    }
}