package com.example.currencyexchanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.example.currencyexchanger.ui.routing.MainNavHost
import com.example.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.example.currencyexchanger.ui.theme.ExchangerTheme
import com.example.currencyexchanger.ui.viewmodels.MainViewModel
import com.example.currencyexchanger.ui.viewmodels.MainViewModelImpl
import com.example.data.datasources.BalanceLocalDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels<MainViewModelImpl>()

    @Inject
    lateinit var dataSource: BalanceLocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                ExchangerTheme.colors.primary.toArgb(),
                darkScrim = ExchangerTheme.colors.primaryDark.toArgb()
            )
        )
        setContent {
            CurrencyExchangerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainNavHost(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}