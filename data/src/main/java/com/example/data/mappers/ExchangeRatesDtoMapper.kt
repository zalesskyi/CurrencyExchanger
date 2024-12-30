package com.example.data.mappers

import com.example.data.models.ExchangeRatesDto
import com.example.domain.models.ExchangeRates

fun ExchangeRatesDto.toDomain() =
    ExchangeRates(
        baseCurrency = baseCurrency,
        rates = rates
    )