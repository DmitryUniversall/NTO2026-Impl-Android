package ru.myitschool.work.ui.screen.main.device

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.core.ui.state.isFetching
import ru.myitschool.work.core.ui.state.toError
import ru.myitschool.work.core.ui.state.toIdle
import ru.myitschool.work.core.ui.state.toLoading
import ru.myitschool.work.core.ui.state.toRefreshing
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.data.repo.BookRepository
import ru.myitschool.work.domain.auth.GetAuthFlowUseCase
import ru.myitschool.work.domain.auth.LogoutUseCase
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.domain.book.CancelBookingUseCase
import ru.myitschool.work.domain.book.GetRoomScheduleUseCase
import ru.myitschool.work.domain.book.SendBookRequestUseCase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DeviceMainViewModel : ViewModel() {
    private val sendBookRequestUseCase by lazy { SendBookRequestUseCase(BookRepository) }
    private val cancelBookingUseCase by lazy { CancelBookingUseCase(BookRepository) }
    private val getRoomScheduleUseCase by lazy { GetRoomScheduleUseCase(BookRepository) }
    private val getAuthFlowUseCase by lazy { GetAuthFlowUseCase(AuthRepository) }
    private val logoutUseCase by lazy { LogoutUseCase(AuthRepository) }

    private val _uiState = MutableStateFlow(DeviceMainState.empty())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadSchedule()
            loadMe()

            withContext(Dispatchers.Default) {
                while (true) {
                    updater()
                    delay(10000)
                }
            }
        }
    }

    fun pageToDate(page: Long): LocalDate = LocalDate.now().plusDays(page)
    fun dateToPage(date: LocalDate): Long = ChronoUnit.DAYS.between(LocalDate.now(), date)

    fun onIntent(intent: DeviceMainIntent) {
        when (intent) {
            is DeviceMainIntent.Logout -> viewModelScope.launch { logoutUseCase.invoke() }
            is DeviceMainIntent.SelectDate -> _uiState.update { it.copy(selectedDate = intent.date) }
            is DeviceMainIntent.Refresh -> viewModelScope.launch { updater(); _uiState.update { it.copy(bookRequest = it.bookRequest.toIdle()) } }
            is DeviceMainIntent.BookForToday -> viewModelScope.launch { bookForToday(); updater() }
            is DeviceMainIntent.CancelBooking -> viewModelScope.launch { cancelBooking(); updater() }
        }
    }

    private suspend fun updater() {
        _uiState.update { it.copy(currentDateTime = LocalDateTime.now()) }
        loadSchedule(refresh = true)
    }

    private suspend fun cancelBooking() {
        if (_uiState.value.cancelBookingRequest.isFetching) {
            Log.w("DeviceMainViewModel", "Already cancelling")
            return
        }

        _uiState.update { it.copy(cancelBookingRequest = it.cancelBookingRequest.toLoading()) }

        _uiState.update {
            it.copy(
                cancelBookingRequest = cancelBookingUseCase.invoke().fold(
                    onSuccess = {
                        ResourceState.Success(Unit)
                    },
                    onFailure = { error ->
                        it.cancelBookingRequest.toError(error.message ?: "Unknown error", error)
                    }
                )
            )
        }
    }

    private suspend fun loadMe() {
        if (_uiState.value.me.isFetching) {
            Log.w("DeviceMainViewModel", "Already loading user")
            return
        }

        _uiState.update { it.copy(me = it.me.toLoading()) }

        val state = getAuthFlowUseCase.invoke().first()

        _uiState.update {
            it.copy(
                me = if (state is AuthState.Authenticated) ResourceState.Success(state.user) else it.me.toError("Unauthorized")
            )
        }
    }

    private suspend fun loadSchedule(refresh: Boolean = false) {
        if (_uiState.value.me.isFetching) {
            Log.w("DeviceMainViewModel", "Already loading schedule")
            return
        }

        _uiState.update { it.copy(schedule = if (refresh) it.schedule.toRefreshing() else it.schedule.toLoading()) }

        _uiState.update {
            it.copy(
                schedule = getRoomScheduleUseCase.invoke().fold(
                    onSuccess = { schedule ->
                        ResourceState.Success(schedule)
                    },
                    onFailure = { error ->
                        it.schedule.toError("Failed to load schedule: ${error.message}", error)
                    }
                )
            )
        }
    }

    private suspend fun bookForToday() {
        if (_uiState.value.bookRequest.isFetching) {
            Log.w("DeviceMainViewModel", "Already booking")
            return
        }

        _uiState.update { it.copy(bookRequest = it.bookRequest.toLoading()) }

        _uiState.update {
            it.copy(
                bookRequest = sendBookRequestUseCase.invoke(null).fold(
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
