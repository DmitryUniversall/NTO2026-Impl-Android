package ru.myitschool.work.ui.screen.main.device.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.core.utils.toHHMM
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import ru.myitschool.work.ui.common.muted
import ru.myitschool.work.ui.common.shimmer
import ru.myitschool.work.ui.common.withShapeBackground
import ru.myitschool.work.ui.screen.main.device.DeviceMainIntent
import ru.myitschool.work.ui.screen.main.device.DeviceMainViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs

private sealed interface BookingLogInfo {
    data class Book(val bookedAt: LocalDateTime, val bookedBy: String) : BookingLogInfo
}

@Composable
private fun BookingLogItem(
    modifier: Modifier = Modifier,
    info: BookingLogInfo
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = modifier
            .padding(8.dp)
            .height(IntrinsicSize.Min)
            .withShapeBackground(
                color = colors.surfaceVariant.muted(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(width = 1.dp, color = colors.onSurface.muted(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (info) {
            is BookingLogInfo.Book -> {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .withShapeBackground(
                            color = colors.error,
                            shape = RoundedCornerShape(percent = 50)
                        ),
                )

                Text(
                    text = info.bookedAt.toLocalTime().toHHMM(),
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.onSurface
                )

                VerticalDivider(modifier = Modifier.fillMaxHeight(0.7f), color = colors.onSurface.muted(alpha = 0.1f))

                Text(
                    text = "Забронировано пользователем ${info.bookedBy}",
                    style = typography.bodyLarge,
                    color = colors.onSurface
                )
            }
        }
    }
}

@Composable
fun ScheduleSelection(
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate,
    daySchedule: ResourceState<RoomDaySchedule>,
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val scrollState = rememberScrollState()

    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                viewModel.onIntent(DeviceMainIntent.SelectDate(date = viewModel.pageToDate(page.toLong())))
            }
    }

    LaunchedEffect(selectedDate) {
        val targetPage = viewModel.dateToPage(selectedDate).toInt()
        if (pagerState.currentPage == targetPage) return@LaunchedEffect

        if (abs(pagerState.currentPage - targetPage) > 1) {
            pagerState.scrollToPage(targetPage)
        } else {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    Column(
        modifier = Modifier
            .border(width = 1.dp, color = colors.onSurface.muted(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScheduleNav(viewModel = viewModel, selectedDate = selectedDate)

        HorizontalPager(
            state = pagerState
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (daySchedule) {
                    is ResourceState.Idle -> {}

                    is ResourceState.Success<RoomDaySchedule> -> {
                        val schedule = daySchedule.data

                        if (schedule.isBooked) {
                            BookingLogItem(
                                info = BookingLogInfo.Book(
                                    bookedAt = LocalDateTime.now(),
                                    bookedBy = schedule.bookedBy!!
                                )
                            )
                        }
                    }

                    is ResourceState.Error -> {
                        Text(
                            text = daySchedule.errorMessage,
                            style = typography.bodyLarge,
                            color = colors.error
                        )
                    }

                    is ResourceState.Loading, is ResourceState.Refreshing -> {
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .fillMaxWidth()
                                .shimmer(
                                    shape = RoundedCornerShape(8.dp),
                                    baseColor = colors.onSurface.copy(alpha = 0.008f),
                                    highlightColor = colors.onSurface.copy(alpha = 0.02f)
                                ),
                        )
                    }
                }
            }
        }
    }
}
