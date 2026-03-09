package ru.myitschool.work.ui.screen.main.device

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun DeviceMainScreen(
    navController: NavController,
    viewModel: DeviceMainViewModel = viewModel()
) {
    Text(text = "TEST")
}
