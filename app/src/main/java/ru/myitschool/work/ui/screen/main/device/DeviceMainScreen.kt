package ru.myitschool.work.ui.screen.main.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myitschool.work.R


@Composable
fun DeviceMainScreen(
    navController: NavController,
    viewModel: DeviceMainViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column() {
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
                text = "Пятница, 3 марта 2026",
                style = typography.headlineSmall,
                color = colors.tertiary
            )
        }
    }
}
