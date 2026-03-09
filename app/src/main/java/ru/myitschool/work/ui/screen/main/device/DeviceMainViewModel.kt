package ru.myitschool.work.ui.screen.main.device

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.core.ui.state.toLoading
import ru.myitschool.work.core.ui.state.toRefreshing
import ru.myitschool.work.core.utils.toIsoString
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.auth.LogoutUseCase
import ru.myitschool.work.domain.book.GetRoomScheduleUseCase
import ru.myitschool.work.domain.book.SendBookRequestUseCase
import ru.myitschool.work.domain.book.entities.BookRequestData
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DeviceMainViewModel : ViewModel() {
    private val sendBookRequestUseCase by lazy { SendBookRequestUseCase(BookRepository) }
    private val getRoomScheduleUseCase by lazy { GetRoomScheduleUseCase(BookRepository) }
    private val logoutUseCase by lazy { LogoutUseCase(AuthRepository) }

    private val _uiState = MutableStateFlow(DeviceMainState.empty())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadSchedule()
        }
    }

    fun pageToDate(page: Long): LocalDate = LocalDate.now().plusDays(page)
    fun dateToPage(date: LocalDate): Long = ChronoUnit.DAYS.between(LocalDate.now(), date)

    fun onIntent(intent: DeviceMainIntent) {
        when (intent) {
            is DeviceMainIntent.SelectDate -> _uiState.update { it.copy(selectedDate = intent.date) }
            is DeviceMainIntent.BookForToday -> viewModelScope.launch { bookForToday(); loadSchedule(refresh = true) }
            is DeviceMainIntent.Logout -> viewModelScope.launch { logoutUseCase.invoke() }
            is DeviceMainIntent.Refresh -> viewModelScope.launch { loadSchedule(refresh = true) }
        }
    }

    private suspend fun loadSchedule(refresh: Boolean = false) {
        _uiState.update { it.copy(schedule = if (refresh) it.schedule.toRefreshing() else it.schedule.toLoading()) }

        delay(2000)

        _uiState.update {
            it.copy(
                schedule = getRoomScheduleUseCase.invoke().fold(
                    onSuccess = { schedule ->
                        ResourceState.Success(schedule)
                    },
                    onFailure = { error ->
                        ResourceState.Error("Failed to load schedule: ${error.message}", error)
                    }
                )
            )
        }
    }

    private suspend fun bookForToday() {
        if (_uiState.value.bookRequest is ResourceState.Loading) {
            Log.w("DeviceMainViewModel", "Already booking")
            return
        }

        _uiState.update { it.copy(bookRequest = it.bookRequest.toLoading()) }

        delay(2000)

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
