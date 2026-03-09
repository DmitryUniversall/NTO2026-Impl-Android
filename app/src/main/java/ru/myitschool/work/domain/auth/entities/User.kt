package ru.myitschool.work.domain.auth.entities

import ru.myitschool.work.domain.book.entities.Place
import java.time.LocalDate

data class User(
    val name: String,
    val photoUrl: String?,
    val userRole: UserRole,
    val booking: Map<LocalDate, Place>
)
