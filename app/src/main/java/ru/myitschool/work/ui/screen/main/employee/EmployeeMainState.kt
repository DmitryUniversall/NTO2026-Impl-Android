package ru.myitschool.work.ui.screen.main.employee

import kotlinx.collections.immutable.PersistentList

sealed interface EmployeeMainState {
    data object Loading: EmployeeMainState
    data class Error(
        val error: String
    ): EmployeeMainState
    data class Data(
        val name: String,
        val photoUrl: String,
        val books: PersistentList<Book>
    ): EmployeeMainState {
        data class Book(
            val date: String,
            val place: String,
        )
    }
}