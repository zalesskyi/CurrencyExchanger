package com.example.network.api

import com.example.data.api.RatesApiService
import com.example.data.models.ExchangeRatesDto
import com.example.network.mappers.toDto
import javax.inject.Inject

class RatesApiServiceImpl
@Inject
constructor(private val api: RatesApi) : RatesApiService {

    override suspend fun getRates(): Result<ExchangeRatesDto> {
        return runCatching { api.getRates().body()!!.toDto() }
    }
}