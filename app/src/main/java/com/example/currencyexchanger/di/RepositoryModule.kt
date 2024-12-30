package com.example.currencyexchanger.di

import com.example.data.repositories.BalanceRepositoryImpl
import com.example.data.repositories.RatesRepositoryImpl
import com.example.domain.repositories.BalanceRepository
import com.example.domain.repositories.RatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRatesRepository(impl: RatesRepositoryImpl): RatesRepository

    @Binds
    abstract fun bindBalanceRepository(impl: BalanceRepositoryImpl): BalanceRepository
}