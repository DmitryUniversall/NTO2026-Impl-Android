package ru.myitschool.work.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import ru.myitschool.work.R
import ru.myitschool.work.core.TestIds

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController
) {
    val isRefreshNeeded = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(MainResult.REFRESH_KEY, false)
        ?.collectAsState()
        ?.value
        ?: false

    LaunchedEffect(isRefreshNeeded) {
        if (isRefreshNeeded) {
            println("!!!!!!!! refresh after book")
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Boolean>(MainResult.REFRESH_KEY)
            viewModel.onIntent(MainIntent.Refresh)
        }
    }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is MainAction.Open -> {
                    navController.navigate(action.destination) {
                        if (action.clearBackStack) {
                            popUpTo(0)
                        }
                    }
                }
            }
        }
    }

    when (val currentState = state) {
        is MainState.Data -> ContentState(viewModel, currentState)
        is MainState.Error -> ErrorState(viewModel, currentState)
        is MainState.Loading -> LoadingState()
    }
}

@Composable
private fun LoadingState() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
private fun ErrorState(
    viewModel: MainViewModel,
    state: MainState.Error
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.testTag(TestIds.Main.ERROR),
            text = state.error,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.testTag(TestIds.Main.REFRESH_BUTTON).fillMaxWidth(),
            onClick = {
                println("!!!!!!!! refresh on click error")
                viewModel.onIntent(MainIntent.Refresh)
            },
        ) {
            Text(stringResource(R.string.main_refresh))
        }
    }
}

@Composable
private fun ContentState(
    viewModel: MainViewModel,
    state: MainState.Data
) {
    Box(
        modifier = Modifier.padding(all = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .testTag(TestIds.Main.PROFILE_IMAGE)
                        .size(64.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.photoUrl)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Text(
                    modifier = Modifier
                        .testTag(TestIds.Main.PROFILE_NAME)
                        .padding(horizontal = 4.dp),
                    text = state.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.testTag(TestIds.Main.REFRESH_BUTTON),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        println("!!!!!!!! refresh on click main")
                        viewModel.onIntent(MainIntent.Refresh)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = stringResource(R.string.main_refresh)
                    )
                }
                IconButton(
                    modifier = Modifier.testTag(TestIds.Main.LOGOUT_BUTTON),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        viewModel.onIntent(MainIntent.Logout)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_logout),
                        contentDescription = stringResource(R.string.main_logout)
                    )
                }
            }
            LazyColumn {
                itemsIndexed(state.books) { index, book ->
                    Row(
                        modifier = Modifier
                            .testTag(TestIds.Main.getIdItemByPosition(index))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.testTag(TestIds.Main.ITEM_PLACE),
                            text = book.place,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            modifier = Modifier.testTag(TestIds.Main.ITEM_DATE),
                            text = book.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.testTag(TestIds.Main.ADD_BUTTON).align(Alignment.BottomEnd),
            onClick = {
                viewModel.onIntent(MainIntent.Add)
            }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = stringResource(R.string.book_add)
            )
        }
    }
}