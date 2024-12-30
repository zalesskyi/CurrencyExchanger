package com.example.currencyexchanger.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.currencyexchanger.R
import com.example.currencyexchanger.ui.common.TopBar
import com.example.currencyexchanger.ui.theme.ExchangerTheme

@Composable
fun MainScreenLoadingContent() {
    Scaffold(
        topBar = {
            TopBar(
                name = stringResource(R.string.currency_converter)
            )
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = ExchangerTheme.colors.primary,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}