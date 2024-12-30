package com.example.domain.usecases

import com.example.domain.base.BaseUseCase
import com.example.domain.models.Currency
import com.example.domain.models.ExchangeRates
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Returns the currencies that are available for user to receive,
 * based on chosen currency to sell
 */
@Singleton
class GetAvailableCurrenciesForReceiveUseCase @Inject constructor()
    : BaseUseCase<GetAvailableCurrenciesForReceiveUseCase.Param, List<Currency>> {

    override fun invoke(param: Param): List<Currency> {
        val (rates, currencyToSell) = param

        return rates.rates.map { it.key }.dropWhile {
            it == currencyToSell
        }
    }

    data class Param(
        val rates: ExchangeRates,
        val currencyToSell: Currency
    )
}