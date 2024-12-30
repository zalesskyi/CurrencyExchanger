package com.example.domain.models

/**
 * Encapsulates the exchange rates for each [Currency] based on [baseCurrency].
 */
data class ExchangeRates(
    val baseCurrency: Currency,
    val rates: Map<Currency, Float>
)