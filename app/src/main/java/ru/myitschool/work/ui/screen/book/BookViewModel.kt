package ru.myitschool.work.ui.screen.book

import android.widget.Toast
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
import ru.myitschool.work.App
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.book.GetDailyBookingDataUseCase
import ru.myitschool.work.domain.book.SendBookRequestUseCase
import ru.myitschool.work.domain.book.entities.BookRequestData

class BookViewModel : ViewModel() {
    private val getDailyBookingDataUseCase by lazy { GetDailyBookingDataUseCase(BookRepository) }
    private val sendBookRequestUseCase by lazy { SendBookRequestUseCase(BookRepository) }

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
                            Toast.makeText(App.context, error.message ?: "Unknown book error", Toast.LENGTH_LONG).show()
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
                getDailyBookingDataUseCase.invoke().fold(
                    onSuccess = { data ->
                        if (data.isEmpty()) return@fold BookState.Empty

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
