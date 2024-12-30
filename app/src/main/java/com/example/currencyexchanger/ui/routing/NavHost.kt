package com.example.currencyexchanger.ui.routing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.currencyexchanger.ui.screens.main.mainScreen
import com.example.currencyexchanger.ui.viewmodels.MainViewModel

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavPage.Main.route
    ) {
        mainScreen(
            viewModel = viewModel
        )
    }
}