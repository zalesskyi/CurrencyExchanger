package com.example.currencyexchanger.di

import com.example.data.api.RatesApiService
import com.example.data.di.NetworkApiServiceProvider
import com.example.network.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideApiService(apiServiceProvider: NetworkApiServiceProvider): RatesApiService {
        return apiServiceProvider.provideRatesApiService()
    }
}
