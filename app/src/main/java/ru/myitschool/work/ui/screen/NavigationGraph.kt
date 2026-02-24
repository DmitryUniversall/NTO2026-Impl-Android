package ru.myitschool.work.ui.screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.GetCodeUseCase
import ru.myitschool.work.ui.nav.AppDestination
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.BookScreenDestination
import ru.myitschool.work.ui.nav.MainScreenDestination
import ru.myitschool.work.ui.screen.auth.AuthScreen
import ru.myitschool.work.ui.screen.book.BookScreen
import ru.myitschool.work.ui.screen.main.MainScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var destination by remember { mutableStateOf<AppDestination?>(null) }
    LaunchedEffect(Unit) {
        val code = GetCodeUseCase(AuthRepository).invoke()
        destination = if (code == null) {
            AuthScreenDestination
        } else {
            MainScreenDestination
        }
    }
    if (destination != null) {
        NavHost(
            modifier = modifier,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            navController = navController,
            startDestination = destination as AppDestination,
        ) {
            composable<AuthScreenDestination> {
                AuthScreen(navController = navController)
            }
            composable<MainScreenDestination> {
                MainScreen(navController = navController)
            }
            composable<BookScreenDestination> {
                BookScreen(navController = navController)
            }
        }
    }
}