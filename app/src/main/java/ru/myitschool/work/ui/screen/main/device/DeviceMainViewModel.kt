package ru.myitschool.work.ui.screen.main.device

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.core.utils.toIsoString
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.book.SendBookRequestUseCase
import ru.myitschool.work.domain.book.entities.BookRequestData
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DeviceMainViewModel : ViewModel() {
    private val sendBookRequestUseCase = SendBookRequestUseCase(BookRepository)

    private val _uiState = MutableStateFlow(DeviceMainState.empty())
    val uiState = _uiState.asStateFlow()

    fun pageToDate(page: Long): LocalDate = LocalDate.now().plusDays(page)
    fun dateToPage(date: LocalDate): Long = ChronoUnit.DAYS.between(LocalDate.now(), date)

    fun onIntent(intent: DeviceMainIntent) {
        when (intent) {
            is DeviceMainIntent.SelectDate -> _uiState.update { it.copy(selectedDate = intent.date) }
            DeviceMainIntent.BookForToday -> viewModelScope.launch { }
        }
    }

    private suspend fun loadSelectedDaySchedule() {

    }

    private suspend fun bookForToday() {
        if (_uiState.value.bookRequest is ResourceState.Loading) {
            Log.w("DeviceMainViewModel", "Already booking")
            return
        }

        _uiState.update { it.copy(bookRequest = ResourceState.Loading) }

        _uiState.update {
            it.copy(
                bookRequest = sendBookRequestUseCase.invoke(
                    BookRequestData(
                        date = LocalDate.now().toIsoString(),
                        placeId = "1"
                    )
                ).fold(
                    onSuccess = {
                        ResourceState.Success(Unit)
                    },
                    onFailure = { error ->
                        ResourceState.Error(error.message ?: "Unknown error", error)
                    }
                )
            )
        }
    }
}
