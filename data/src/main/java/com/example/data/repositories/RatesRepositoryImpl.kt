package com.example.data.repositories

import com.example.data.datasources.RatesRemoteDataSource
import com.example.domain.models.ExchangeRates
import com.example.domain.repositories.RatesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RatesRemoteDataSource
) : RatesRepository {

    override suspend fun getRates(): Result<ExchangeRates> = remoteDataSource.getRates()
}