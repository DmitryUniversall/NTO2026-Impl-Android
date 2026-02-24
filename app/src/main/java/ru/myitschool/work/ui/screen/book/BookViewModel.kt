package ru.myitschool.work.ui.screen.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.book.GetBookingDataUseCase
import ru.myitschool.work.domain.book.SendBookRequestUseCase
import ru.myitschool.work.domain.book.entities.BookRequestData
import kotlin.getValue

class BookViewModel : ViewModel() {
    private val bookRepository by lazy { BookRepository(AuthRepository) }
    private val getBookingDataUseCase by lazy { GetBookingDataUseCase(bookRepository) }
    private val sendBookRequestUseCase by lazy { SendBookRequestUseCase(bookRepository) }
    private val _uiState = MutableStateFlow<BookState>(BookState.Loading)
    val uiState: StateFlow<BookState> = _uiState.asStateFlow()

    private val _actionFlow: MutableSharedFlow<BookAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<BookAction> = _actionFlow

    init {
        refresh()
    }

    fun onIntent(intent: BookIntent) {
        when (intent) {
            is BookIntent.Refresh -> {
                refresh()
            }

            is BookIntent.Add -> {
                viewModelScope.launch {
                    sendBookRequestUseCase.invoke(
                        BookRequestData(
                            date = intent.date,
                            placeId = intent.placeId
                        )
                    ).fold(
                        onSuccess = {
                            _actionFlow.emit(BookAction.BackWithSuccess)
                        },
                        onFailure = { error ->
                            error.printStackTrace()
                        }
                    )
                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { BookState.Loading }
            _uiState.update {
                getBookingDataUseCase.invoke().fold(
                    onSuccess = { data ->
                        if (data.isEmpty()) {
                            BookState.Empty
                        } else {
                            BookState.Data(
                                items = data.map { item ->
                                    BookState.Data.Item(
                                        date = item.date,
                                        places = item.places.map { place ->
                                            BookState.Data.Place(
                                                id = place.id,
                                                name = place.name
                                            )
                                        }.toPersistentList()
                                    )
                                }.toPersistentList()
                            )
                        }
                    },
                    onFailure = { error ->
                        BookState.Error(
                            error = error.message.orEmpty()
                        )
                    }
                )
            }
        }
    }
}