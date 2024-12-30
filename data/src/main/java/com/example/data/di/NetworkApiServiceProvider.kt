package com.example.data.di

import com.example.data.api.RatesApiService

interface NetworkApiServiceProvider {

    fun provideRatesApiService(): RatesApiService
}