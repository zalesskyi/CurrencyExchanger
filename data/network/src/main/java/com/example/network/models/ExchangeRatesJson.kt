package com.example.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesJson(
    @SerialName("base")
    val baseCurrency: String,
    @SerialName("rates")
    val rates: Map<String, Float>
)