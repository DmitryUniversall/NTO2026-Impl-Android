package ru.myitschool.work.ui.screen.main.device.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.myitschool.work.R
import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.core.ui.state.hasError
import ru.myitschool.work.core.ui.state.isLoading
import ru.myitschool.work.core.ui.state.whenHasAnyData
import ru.myitschool.work.core.ui.state.whenHasAnyError
import ru.myitschool.work.core.ui.state.whenError
import ru.myitschool.work.core.ui.state.whenFetchingFirstTime
import ru.myitschool.work.core.ui.state.whenLoading
import ru.myitschool.work.domain.auth.entities.User
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import ru.myitschool.work.domain.main.entities.isBooked
import ru.myitschool.work.ui.common.components.button.PrimaryGenericButton
import ru.myitschool.work.ui.common.shimmer
import ru.myitschool.work.ui.common.withShapeBackground
import ru.myitschool.work.ui.screen.main.device.DeviceMainIntent
import ru.myitschool.work.ui.screen.main.device.DeviceMainViewModel
import ru.myitschool.work.ui.theme.SuccessColor
import java.time.LocalDate

@Composable
private fun BookButton(
    modifier: Modifier = Modifier,
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate,
    bookRequestState: ResourceState<Unit>,
    daySchedule: RoomDaySchedule,
    isScheduleActual: Boolean
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val bookAllowed = selectedDate == LocalDate.now() && !bookRequestState.isLoading && !daySchedule.isBooked && isScheduleActual

    PrimaryGenericButton(
        modifier = modifier
            .fillMaxWidth(1f)
            .alpha(if (bookAllowed) 1f else 0.7f),
        enabled = bookAllowed,
        onClick = {
            viewModel.onIntent(DeviceMainIntent.BookForToday)
        },
        text = stringResource(R.string.book_for_today),
        trailing = {
            bookRequestState.whenLoading { CircularProgressIndicator(modifier = Modifier.size(16.dp)) }

            bookRequestState.whenError { state ->
                Text(
                    modifier = Modifier,
                    text = "(${stringResource(R.string.error)}: ${state.errorMessage})",
                    style = typography.bodySmall,
                    color = colors.error
                )
            }
        }
    )
}

@Composable
private fun UnbookButton(
    modifier: Modifier = Modifier,
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate,
    cancelBookingRequestState: ResourceState<Unit>,
    isScheduleActual: Boolean
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val cancelAllowed = selectedDate == LocalDate.now() && !cancelBookingRequestState.isLoading && isScheduleActual

    PrimaryGenericButton(
        modifier = modifier
            .fillMaxWidth(1f)
            .alpha(if (cancelAllowed) 1f else 0.7f),
        enabled = cancelAllowed,
        onClick = {
            viewModel.onIntent(DeviceMainIntent.CancelBooking)
        },
        text = stringResource(R.string.cancel_booking),
        trailing = {
            cancelBookingRequestState.whenLoading { CircularProgressIndicator(modifier = Modifier.size(16.dp)) }

            cancelBookingRequestState.whenHasAnyError { state ->
                Text(
                    text = "(${stringResource(R.string.error)}: ${state.errorMessage})",
                    style = typography.bodySmall,
                    color = colors.error
                )
            }
        }
    )
}

@Composable
fun TopRowSelection(
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate,
    schedule: ResourceState<Map<LocalDate, RoomDaySchedule>>,
    bookRequestState: ResourceState<Unit>,
    cancelBookingRequestState: ResourceState<Unit>,
    me: ResourceState<User>
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        schedule.whenFetchingFirstTime {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .shimmer(
                        shape = RoundedCornerShape(8.dp),
                        baseColor = colors.onSurface.copy(alpha = 0.08f),
                        highlightColor = colors.onSurface.copy(alpha = 0.2f)
                    )
            )
        }

        schedule.whenHasAnyData { data ->
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(240.dp),
                painter = painterResource(R.drawable.room),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val daySchedule = data[selectedDate]!!  // TODO: Unsafe

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .withShapeBackground(color = if (daySchedule.isBooked) colors.error else SuccessColor, shape = RoundedCornerShape(8.dp))  // SuccessColor?
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(if (daySchedule.isBooked) R.string.room_booked else R.string.room_free),
                        style = typography.displaySmall,
                        color = colors.onError
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    me.whenHasAnyError { state ->  // Fetching cached user should not cause an error, but just in case
                        Text(
                            text = state.errorMessage,
                            style = typography.bodyLarge,
                            color = colors.error
                        )
                    }

                    me.whenFetchingFirstTime {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .shimmer(
                                    shape = RoundedCornerShape(8.dp),
                                    baseColor = colors.onSurface.copy(alpha = 0.08f),
                                    highlightColor = colors.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }

                    me.whenHasAnyData { user ->
                        if (daySchedule is RoomDaySchedule.Bookend && daySchedule.bookedBy == user.name) {
                            UnbookButton(
                                modifier = Modifier.weight(1f),
                                viewModel = viewModel,
                                selectedDate = selectedDate,
                                cancelBookingRequestState = cancelBookingRequestState,
                                isScheduleActual = !schedule.hasError
                            )
                        } else {
                            BookButton(
                                modifier = Modifier.weight(1f),
                                viewModel = viewModel,
                                selectedDate = selectedDate,
                                bookRequestState = bookRequestState,
                                daySchedule = daySchedule,
                                isScheduleActual = !schedule.hasError
                            )
                        }
                    }
                }
            }
        }
    }
}
