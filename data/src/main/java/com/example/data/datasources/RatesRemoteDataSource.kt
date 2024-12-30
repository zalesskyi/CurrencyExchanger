package com.example.data.datasources

import com.example.data.api.RatesApiService
import com.example.data.mappers.toDomain
import com.example.domain.models.ExchangeRates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRemoteDataSource @Inject constructor(
    private val apiService: RatesApiService
) {

    suspend fun getRates(): Result<ExchangeRates> {
        return apiService.getRates().map {
            it.toDomain()
        }
    }
}