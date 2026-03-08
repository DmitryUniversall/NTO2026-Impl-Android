package ru.myitschool.work.ui.screen.init

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myitschool.work.R

@Composable
fun InitScreen(
    viewModel: InitScreenViewModel = viewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is InitAction.Navigate -> navController.navigate(action.destination) {
                    popUpTo(0)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.init_welcome),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            when (val currentState = state) {
                is InitState.Idle -> IdleView()
                is InitState.Loading -> LoadingView()
                is InitState.Success -> SuccessView()
                is InitState.Error -> ErrorView(viewModel, currentState)
            }
        }
    }

}

@Composable
fun IdleView() {
}

@Composable
fun LoadingView() {
    CircularProgressIndicator(
        modifier = Modifier.size(64.dp)
    )
}

@Composable
fun SuccessView() {
    Text(
        text = stringResource(R.string.success),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ErrorView(
    viewModel: InitScreenViewModel,
    state: InitState.Error
) {
    Text(
        text = state.errorMessage,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.size(16.dp))

    Button(
        onClick = {
            viewModel.onIntent(InitIntent.Refresh)
        },
    ) {
        Text(stringResource(R.string.main_refresh))
    }
}
