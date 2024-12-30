package com.example.currencyexchanger.utils

import java.util.Locale

fun Float.formatAmountToString() =
    String.format(Locale.getDefault(), "%.2f", this)