package ru.myitschool.work.ui.screen.main.device

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.myitschool.work.R
import ru.myitschool.work.core.TestIds
import ru.myitschool.work.core.ui.state.whenError
import ru.myitschool.work.core.utils.toHHMM
import ru.myitschool.work.ui.common.muted
import ru.myitschool.work.ui.screen.main.device.ui.ScheduleSelection
import ru.myitschool.work.ui.screen.main.device.ui.TopRowSelection
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun DeviceMainScreen(
    viewModel: DeviceMainViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val uiState by viewModel.uiState.collectAsState()
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern(
            "EEEE, d MMMM yyyy 'г.'",
            Locale.forLanguageTag("ru")
        )
    }

    Column(
        modifier = Modifier
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.currentDateTime.toLocalTime().toHHMM(),
                    style = typography.displayMedium,
                    color = colors.onBackground
                )

                Text(
                    text = dateFormatter.format(uiState.currentDateTime),
                    style = typography.headlineSmall,
                    color = colors.onBackground.muted()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier.testTag(TestIds.Main.LOGOUT_BUTTON),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        viewModel.onIntent(DeviceMainIntent.Refresh)
                    },
                    enabled = !uiState.isFetching
                ) {
                    if (uiState.isFetching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = stringResource(R.string.main_logout)
                        )
                    }
                }

                IconButton(
                    modifier = Modifier.testTag(TestIds.Main.LOGOUT_BUTTON),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        viewModel.onIntent(DeviceMainIntent.Logout)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_logout),
                        contentDescription = stringResource(R.string.main_logout)
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .wrapContentHeight()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.schedule.whenError { errorMessage, _, _ ->
                    Text(
                        text = errorMessage,
                        style = typography.bodyLarge,
                        color = colors.error
                    )
                }

                TopRowSelection(
                    viewModel = viewModel,
                    selectedDate = uiState.selectedDate,
                    schedule = uiState.schedule,
                    bookRequestState = uiState.bookRequest,
                    cancelBookingRequestState = uiState.cancelBookingRequest,
                    me = uiState.me
                )
                ScheduleSelection(viewModel = viewModel, selectedDate = uiState.selectedDate, schedule = uiState.schedule)
            }
        }
    }
}
