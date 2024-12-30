package com.example.data.api

import com.example.data.models.ExchangeRatesDto

interface RatesApiService {

    suspend fun getRates(): Result<ExchangeRatesDto>
}