package ru.myitschool.work.core

object Constants {
    const val HOST = "http://10.0.2.2:8080"

    const val AUTH_URL = "auth"
    const val LOGIN_URL = "$AUTH_URL/login"

    const val USERS_URL = "users"
    const val GET_ME_URL = "$USERS_URL/me"

    const val BOOKING_URL = "booking"
    const val GET_AVAILABLE_PLACES_URL = "$BOOKING_URL/available"
    const val BOOK_URL = "$BOOKING_URL/book"
    const val GET_USER_BOOKINGS_URL = "$BOOKING_URL/my"
}
