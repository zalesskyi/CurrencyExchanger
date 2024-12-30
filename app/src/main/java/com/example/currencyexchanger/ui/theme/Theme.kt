package com.example.currencyexchanger.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ExchangerTheme.colors.primaryDark,
    secondary = ExchangerTheme.colors.secondaryDark,
    tertiary = ExchangerTheme.colors.tertiaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = ExchangerTheme.colors.primary,
    secondary = ExchangerTheme.colors.secondary,
    tertiary = ExchangerTheme.colors.tertiary
)

object ExchangerTheme {
    val colors = ExchangerColors()
}

@Composable
fun CurrencyExchangerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}