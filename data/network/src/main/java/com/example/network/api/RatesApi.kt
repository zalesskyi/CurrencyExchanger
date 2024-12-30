package com.example.network.api

import com.example.network.models.ExchangeRatesJson
import retrofit2.Response
import retrofit2.http.POST

interface RatesApi {

    @POST("currency-exchange-rates")
    suspend fun getRates(): Response<ExchangeRatesJson>
}