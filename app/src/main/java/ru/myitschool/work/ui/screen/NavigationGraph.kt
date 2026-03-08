package ru.myitschool.work.ui.screen

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.myitschool.work.R
import ru.myitschool.work.ui.common.dialogs.SimpleMessageDialog
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.BookScreenDestination
import ru.myitschool.work.ui.nav.InitScreenDestination
import ru.myitschool.work.ui.nav.MainScreenDestination
import ru.myitschool.work.ui.screen.auth.AuthScreen
import ru.myitschool.work.ui.screen.book.BookScreen
import ru.myitschool.work.ui.screen.init.InitScreen
import ru.myitschool.work.ui.screen.main.MainScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    viewModel: NavigationGraphViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    var showDialog by remember { mutableStateOf<NavigationGraphDialog?>(null) }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is NavigationGraphAction.Navigate -> {
                    Log.i("NavigationGraphAction", "Navigate to: ${action.destination::class.simpleName}")
                    navController.navigate(action.destination) {
                        if (action.clearBackStack) popUpTo(0)
                    }
                }

                is NavigationGraphAction.ShowDialog -> {
                    Log.i("NavigationGraphAction", "Show dialog: ${action.dialogType}")
                    showDialog = action.dialogType
                }
            }
        }
    }

    showDialog?.let { dialog ->
        when (dialog) {
            is NavigationGraphDialog.AuthRequired -> SimpleMessageDialog(
                title = stringResource(R.string.auth_required_dialog_title),
                message = stringResource(R.string.auth_required_dialog_message),
            ) {
                navController.navigate(AuthScreenDestination) {
                    popUpTo(0)
                }

                showDialog = null
            }

            is NavigationGraphDialog.Message -> SimpleMessageDialog(
                title = dialog.title,
                message = dialog.message,
                onDismiss = { showDialog = null }
            )
        }
    }

    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        navController = navController,
        startDestination = InitScreenDestination,
    ) {
        composable<InitScreenDestination> {
            InitScreen(navController = navController)
        }
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
