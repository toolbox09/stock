package com.example.stock.screens.root

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stock.screens.login.LoginScreen
import com.example.stock.screens.main.MainScreen


@Composable
fun AuthRouteScreen() {
    val userViewModel : RootViewModel = viewModel(factory = rootViewModelFactory())
    val state by userViewModel.state.collectAsState()
    when {
        state.user != null -> MainScreen(userViewModel)
        else -> LoginScreen(userViewModel)
    }
}