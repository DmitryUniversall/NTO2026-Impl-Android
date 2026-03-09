package ru.myitschool.work.ui.screen.main.device.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.myitschool.work.R
import ru.myitschool.work.core.ui.state.ResourceState
import ru.myitschool.work.domain.main.entities.RoomDaySchedule
import ru.myitschool.work.ui.common.components.button.PrimaryGenericButton
import ru.myitschool.work.ui.common.shimmer
import ru.myitschool.work.ui.screen.main.device.DeviceMainIntent
import ru.myitschool.work.ui.screen.main.device.DeviceMainViewModel
import ru.myitschool.work.ui.theme.SuccessColor
import java.time.LocalDate

@Composable
fun TopRowSelection(
    viewModel: DeviceMainViewModel,
    selectedDate: LocalDate,
    daySchedule: ResourceState<RoomDaySchedule>,
    bookRequestState: ResourceState<Unit>
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(shape = RoundedCornerShape(8.dp)),
    ) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (daySchedule) {
                is ResourceState.Idle -> {}

                is ResourceState.Success<RoomDaySchedule> -> {
                    val schedule = daySchedule.data
                    val bookAllowed = selectedDate == LocalDate.now() && !schedule.isBooked && bookRequestState !is ResourceState.Loading

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(color = if (schedule.isBooked) colors.error else SuccessColor)  // ?
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(if (schedule.isBooked) R.string.room_booked else R.string.room_free),
                            style = typography.displaySmall,
                            color = colors.onError
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PrimaryGenericButton(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .fillMaxHeight()
                                .alpha(if (bookAllowed) 1f else 0.7f),
                            enabled = bookAllowed,
                            onClick = {
                                viewModel.onIntent(DeviceMainIntent.BookForToday)
                            },
                            text = stringResource(R.string.book_for_today),
                            trailing = {
                                if (bookRequestState is ResourceState.Loading) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                                }
                            }
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
                            .weight(1f)
                            .fillMaxWidth()
                            .shimmer(
                                shape = RectangleShape,
                                baseColor = colors.onSurface.copy(alpha = 0.008f),
                                highlightColor = colors.onSurface.copy(alpha = 0.02f)
                            )
                    )
                }
            }
        }
    }
}
