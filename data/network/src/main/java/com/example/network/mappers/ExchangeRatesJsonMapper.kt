package com.example.network.mappers

import com.example.data.models.ExchangeRatesDto
import com.example.network.models.ExchangeRatesJson

fun ExchangeRatesJson.toDto() = ExchangeRatesDto(
    baseCurrency = baseCurrency,
    rates = rates
)