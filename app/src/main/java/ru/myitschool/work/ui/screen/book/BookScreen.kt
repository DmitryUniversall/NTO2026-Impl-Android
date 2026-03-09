package ru.myitschool.work.ui.screen.book

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.myitschool.work.R
import ru.myitschool.work.core.TestIds
import ru.myitschool.work.ui.screen.main.employee.EmployeeMainResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BookScreen(
    viewModel: BookViewModel = viewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actionFlow.collect { action ->
            when (action) {
                is BookAction.Back -> navController.popBackStack()
                is BookAction.BackWithSuccess -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(EmployeeMainResult.REFRESH_KEY, true)
                    navController.popBackStack()
                }
            }
        }
    }

    Column {
        IconButton(
            modifier = Modifier.testTag(TestIds.Book.BACK_BUTTON),
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
                navController.popBackStack()
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.book_back)
            )
        }

        when (val currentState = state) {
            is BookState.Data -> ContentState(viewModel, currentState)
            is BookState.Error -> ErrorState(viewModel, currentState)
            is BookState.Loading -> LoadingState()
            is BookState.Empty -> EmptyState()
        }
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
private fun EmptyState() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.testTag(TestIds.Book.EMPTY),
            text = stringResource(R.string.book_empty),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
        )
    }
}

@Composable
private fun ErrorState(
    viewModel: BookViewModel,
    state: BookState.Error
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.testTag(TestIds.Book.ERROR),
            text = state.error,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.testTag(TestIds.Book.REFRESH_BUTTON).fillMaxWidth(),
            onClick = {
                viewModel.onIntent(BookIntent.Refresh)
            },
        ) {
            Text(stringResource(R.string.main_refresh))
        }
    }
}

@Composable
private fun ContentState(
    viewModel: BookViewModel,
    state: BookState.Data
) {
    val navController = rememberNavController()
    val startDestination = SelectedTabDestination(index = 0)
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(startDestination.index)
    }
    var selectedPlaceId by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    Box {
        Column {
            PrimaryTabRow(
                modifier = Modifier,
                selectedTabIndex = selectedDestination,
            ) {
                state.items.forEachIndexed { index, destination ->
                    Tab(
                        modifier = Modifier
                            .testTag(TestIds.Book.getIdDateItemByPosition(index)),
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(
                                route = SelectedTabDestination(index = index)
                            ) {
                                launchSingleTop = true
                            }
                            selectedDestination = index
                        },
                        text = {
                            Text(
                                modifier = Modifier.testTag(TestIds.Book.ITEM_DATE),
                                text = LocalDate.parse(destination.date)
                                    .format(
                                        DateTimeFormatter.ofPattern(
                                            "dd.MM"
                                        )
                                    ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            TabNavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = startDestination,
                state = state,
                onPlaceSelected = { id ->
                    selectedPlaceId = id
                }
            )
        }
        Box(
            modifier = Modifier
                .padding(all = 24.dp)
                .align(Alignment.BottomEnd),
        ) {
            FloatingActionButton(
                modifier = Modifier.testTag(TestIds.Book.BOOK_BUTTON),
                onClick = {
                    val id = selectedPlaceId
                    if (id != null) {
                        viewModel.onIntent(
                            BookIntent.Add(
                                date = state.items[selectedDestination].date,
                                placeId = id
                            )
                        )
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = stringResource(R.string.book_add)
                )
            }
        }
    }
}

@Composable
fun TabNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: SelectedTabDestination,
    state: BookState.Data,
    onPlaceSelected: (String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<SelectedTabDestination> {
            val index = it.toRoute<SelectedTabDestination>().index
            val data = state.items[index]
            val (selectedOption, onOptionSelected) = remember {
                mutableStateOf(data.places[0].id)
            }
            onPlaceSelected.invoke(selectedOption)

            Column(modifier.selectableGroup()) {
                data.places.forEachIndexed { index, place ->
                    val isSelected = place.id == selectedOption
                    Row(
                        Modifier
                            .testTag(TestIds.Book.getIdPlaceItemByPosition(index))
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = isSelected,
                                onClick = {
                                    onPlaceSelected(place.id)
                                    onOptionSelected(place.id)
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier.testTag(TestIds.Book.ITEM_PLACE_SELECTOR),
                            selected = isSelected,
                            onClick = null
                        )
                        Text(
                            modifier = Modifier
                                .testTag(TestIds.Book.ITEM_PLACE_TEXT)
                                .padding(start = 16.dp),
                            text = place.name,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

@Serializable
data class SelectedTabDestination(
    val index: Int
)