package ru.myitschool.work.domain.main.entities

data class MainInfoEntity(
    val name: String,
    val photoUrl: String,
    val book: List<Book>
) {
    data class Book(
        val date: String,
        val place: String,
    )
}
