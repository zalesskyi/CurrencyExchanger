package com.example.domain.repositories

import com.example.domain.models.ExchangeRates

interface RatesRepository {

    suspend fun getRates(): Result<ExchangeRates>
}