package ru.myitschool.work.ui.screen.book

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

sealed interface BookState {
    data object Loading : BookState

    data object Empty : BookState
    data class Error(
        val error: String
    ) : BookState

    data class Data(
        val items: PersistentList<Item>
    ) : BookState {

        data class Item(
            val date: String,
            val places: PersistentList<Place>,
        )

        data class Place(
            val id: String,
            val name: String,
        )
    }
}