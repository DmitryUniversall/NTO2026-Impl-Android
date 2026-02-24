package ru.myitschool.work.core

object TestIds {
    object Auth {
        const val ERROR = "auth_error"
        const val SIGN_BUTTON = "auth_sign_button"
        const val CODE_INPUT = "auth_code_input"
    }
    object Main {
        const val ERROR = "main_error"
        const val ADD_BUTTON = "main_add_button"
        const val REFRESH_BUTTON = "main_refresh_button"
        const val LOGOUT_BUTTON = "main_logout_button"
        const val PROFILE_IMAGE = "main_image"
        const val PROFILE_NAME = "main_name"
        const val ITEM_PLACE = "main_item_place"
        const val ITEM_DATE = "main_item_date"

        fun getIdItemByPosition(position: Int) = "main_book_pos_$position"
    }

    object Book {
        const val ERROR = "book_error"
        const val EMPTY = "book_empty"
        const val REFRESH_BUTTON = "book_refresh_button"
        const val BACK_BUTTON = "book_back_button"
        const val BOOK_BUTTON = "book_book_button"
        const val ITEM_DATE = "book_date"
        const val ITEM_PLACE_TEXT = "book_place_text"
        const val ITEM_PLACE_SELECTOR = "book_place_selector"

        fun getIdDateItemByPosition(position: Int) = "book_date_pos_$position"
        fun getIdPlaceItemByPosition(position: Int) = "book_place_pos_$position"
    }
}