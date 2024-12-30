package com.example.network.di

import com.example.data.di.NetworkApiServiceProvider
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
abstract class NetworkModule {

   /* @Binds
    abstract fun bindNetworkApiProvider(impl: NetworkApiServiceProviderImpl): NetworkApiServiceProvider
*/
    companion object {

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            val json = Json { ignoreUnknownKeys = true }
            return Retrofit.Builder()
                .baseUrl("https://developers.paysera.com/tasks/api/")
                .addConverterFactory(
                    json.asConverterFactory(
                        MediaType.parse("application/json")!!)
                )
                .build()
        }

        @Provides
        @Singleton
        fun provideNetworkApiProvider(retrofit: Retrofit): NetworkApiServiceProvider =
            NetworkApiServiceProviderImpl(retrofit)
    }
}