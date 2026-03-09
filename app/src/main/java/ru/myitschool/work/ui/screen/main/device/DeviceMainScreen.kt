package ru.myitschool.work.ui.screen.main.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myitschool.work.ui.common.muted
import ru.myitschool.work.ui.screen.main.device.ui.ScheduleSelection
import ru.myitschool.work.ui.screen.main.device.ui.TopRowSelection


@Composable
fun DeviceMainScreen(
    navController: NavController,
    viewModel: DeviceMainViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "15:00",
                style = typography.displayMedium,
                color = colors.onBackground
            )

            Text(
                text = "Пятница, 3 марта 2026 г.",
                style = typography.headlineSmall,
                color = colors.onBackground.muted()
            )
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
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TopRowSelection(viewModel = viewModel, selectedDate = uiState.selectedDate, daySchedule = uiState.selectedDaySchedule, bookRequestState = uiState.bookRequest)
                ScheduleSelection(viewModel = viewModel, selectedDate = uiState.selectedDate, daySchedule = uiState.selectedDaySchedule)
            }
        }
    }
}
