package com.example.currencyexchanger.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Green = Color(0xFF43a047)

val VibrantBlue = Color(0xFF2596be)
val VibrantOrange = Color(0xFFFFB74D)
val VibrantGreen = Color(0xFF8BC34A)

data class ExchangerColors(
    val primary: Color = VibrantBlue,
    val secondary: Color = VibrantOrange,
    val tertiary: Color = VibrantGreen,

    val primaryDark: Color = Purple80,
    val secondaryDark: Color = PurpleGrey80,
    val tertiaryDark: Color = Pink80
)