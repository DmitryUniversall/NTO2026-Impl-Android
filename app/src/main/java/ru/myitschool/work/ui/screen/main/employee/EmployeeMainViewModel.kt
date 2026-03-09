package ru.myitschool.work.ui.screen.main.employee

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
import ru.myitschool.work.domain.auth.LogoutUseCase
import ru.myitschool.work.domain.main.GetMainDataUseCase
import ru.myitschool.work.ui.nav.BookScreenDestination
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EmployeeMainViewModel : ViewModel() {
    private val getMainDataUseCase by lazy { GetMainDataUseCase(AuthRepository) }
    private val logoutUseCase by lazy { LogoutUseCase(AuthRepository) }

    private val _uiState = MutableStateFlow<EmployeeMainState>(EmployeeMainState.Loading)
    val uiState: StateFlow<EmployeeMainState> = _uiState.asStateFlow()

    private val _actionFlow: MutableSharedFlow<EmployeeMainAction> = MutableSharedFlow()
    val actionFlow: SharedFlow<EmployeeMainAction> = _actionFlow

    init {
        refresh()
    }

    fun onIntent(intent: EmployeeMainIntent) {
        when (intent) {
            is EmployeeMainIntent.Add -> {
                viewModelScope.launch {
                    _actionFlow.emit(EmployeeMainAction.Navigate(BookScreenDestination))
                }
            }

            is EmployeeMainIntent.Refresh -> {
                refresh(fetch = true)
            }

            is EmployeeMainIntent.Logout -> {
                viewModelScope.launch {
                    logoutUseCase.invoke()
                }
            }
        }
    }

    private fun refresh(fetch: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { EmployeeMainState.Loading }
            _uiState.update {
                getMainDataUseCase.invoke(fetch = fetch).fold(
                    onSuccess = { data ->
                        EmployeeMainState.Data(
                            name = data.name,
                            photoUrl = data.photoUrl,
                            books = data.book.map { book ->
                                EmployeeMainState.Data.Book(
                                    date = LocalDate
                                        .parse(book.date)
                                        .format(
                                            DateTimeFormatter.ofPattern(DATE_FORMAT)
                                        ),
                                    place = book.place
                                )
                            }.toPersistentList()
                        )
                    },
                    onFailure = { error ->
                        EmployeeMainState.Error(
                            error = error.message?.takeIf { it.isNotBlank() } ?: "Unknown error"
                        )
                    }
                )
            }
        }
    }

    private companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
    }
}
