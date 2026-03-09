package ru.myitschool.work.domain.book.entities

data class BookingData(
    val date: String,
    val places: List<Place>
)
