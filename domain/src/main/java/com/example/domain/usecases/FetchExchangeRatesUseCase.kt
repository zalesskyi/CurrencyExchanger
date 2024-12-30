package com.example.domain.usecases

import com.example.domain.base.BaseFlowUseCase
import com.example.domain.models.ExchangeRates
import com.example.domain.repositories.RatesRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

/**
 * Fetches the exchange rates from server each 5 seconds.
 */
@Singleton
class FetchExchangeRatesUseCase @Inject constructor(
    private val repository: RatesRepository
) : BaseFlowUseCase<ExchangeRates> {

    override fun invoke(): Flow<Result<ExchangeRates>> {
        return flow {
            while (currentCoroutineContext().isActive) {
                emit(repository.getRates())
                delay(5.seconds)
            }
        }
    }
}