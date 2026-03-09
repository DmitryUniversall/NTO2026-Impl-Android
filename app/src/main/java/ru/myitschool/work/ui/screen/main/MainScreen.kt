package ru.myitschool.work.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myitschool.work.R
import ru.myitschool.work.ui.screen.MainScreenIntent
import ru.myitschool.work.ui.screen.main.device.DeviceMainScreen
import ru.myitschool.work.ui.screen.main.employee.EmployeeMainScreen

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val currentState = uiState) {
        is MainScreenState.Loading -> LoadingView()
        is MainScreenState.Error -> ErrorView(viewModel, currentState)
        is MainScreenState.Device -> DeviceMainScreen()
        is MainScreenState.Employee -> EmployeeMainScreen(navController = navController)
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
private fun ErrorView(
    viewModel: MainScreenViewModel,
    state: MainScreenState.Error
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                viewModel.onIntent(MainScreenIntent.Refresh)
            },
        ) {
            Text(stringResource(R.string.main_refresh))
        }
    }
}
