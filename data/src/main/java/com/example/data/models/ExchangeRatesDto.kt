package com.example.data.models

data class ExchangeRatesDto(
    val baseCurrency: String,
    val rates: Map<String, Float>
)