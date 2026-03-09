package ru.myitschool.work.ui.screen.main.device.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import ru.myitschool.work.core.ui.state.isLoading
import ru.myitschool.work.core.ui.state.whenData
import ru.myitschool.work.core.ui.state.whenError
import ru.myitschool.work.core.ui.state.whenLoading
import ru.myitschool.work.core.ui.state.whenState
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import ru.myitschool.work.ui.common.components.button.PrimaryGenericButton
import ru.myitschool.work.ui.common.shimmer
import ru.myitschool.work.ui.common.withShapeBackground
import ru.myitschool.work.ui.screen.main.device.DeviceMainIntent
import ru.myitschool.work.ui.screen.main.device.DeviceMainViewModel
import ru.myitschool.work.ui.theme.SuccessColor
import java.time.LocalDate

@Composable
fun TopRowSelection(
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate,
    schedule: ResourceState<Map<LocalDate, RoomDaySchedule>>,
    bookRequestState: ResourceState<Unit>
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
        schedule.whenError { errorMessage, _, _ ->
            Text(
                text = errorMessage,
                style = typography.bodyLarge,
                color = colors.error
            )
        }

        schedule.whenState(
            ResourceState.Loading::class,
            ResourceState.Refreshing::class,
            containsData = false
        ) {
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

        schedule.whenData { data ->
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
                val bookAllowed = selectedDate == LocalDate.now() && !daySchedule.isBooked && !bookRequestState.isLoading

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
                    PrimaryGenericButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(1f)
                            .alpha(if (bookAllowed) 1f else 0.7f),
                        enabled = bookAllowed,
                        onClick = {
                            viewModel.onIntent(DeviceMainIntent.BookForToday)
                        },
                        text = stringResource(R.string.book_for_today),
                        trailing = {
                            bookRequestState.whenLoading { CircularProgressIndicator(modifier = Modifier.size(16.dp)) }

                            bookRequestState.whenError { errorMessage, _, _ ->
                                Text(
                                    modifier = Modifier
                                        .weight(0.5f),
                                    text = "(${stringResource(R.string.error)}: $errorMessage)",
                                    style = typography.bodySmall,
                                    color = colors.error
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
