package com.example.currencyexchanger.ui.common

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.currencyexchanger.ui.theme.ExchangerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    name: String
) {
    CenterAlignedTopAppBar(
        modifier = modifier.verticalScroll(rememberScrollState()),
        title = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ExchangerTheme.colors.primary
        ),
        windowInsets = WindowInsets(0.dp)
    )
}