package com.example.network.di

import com.example.data.di.NetworkApiServiceProvider
import com.example.network.api.RatesApi
import com.example.network.api.RatesApiServiceImpl
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkApiServiceProviderImpl @Inject constructor(
    private val retrofit: Retrofit
) : NetworkApiServiceProvider {

    override fun provideRatesApiService() =
        RatesApiServiceImpl(retrofit.create(RatesApi::class.java))
}