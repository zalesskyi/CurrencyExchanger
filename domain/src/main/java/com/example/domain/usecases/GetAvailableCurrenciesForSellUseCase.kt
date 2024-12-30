package com.example.domain.usecases

import com.example.domain.base.BaseUseCase
import com.example.domain.models.Balances
import com.example.domain.models.Currency
import com.example.domain.models.ExchangeRates
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Returns the currencies that are available for user to sell,
 * based on chosen currency to receive
 */
@Singleton
class GetAvailableCurrenciesForSellUseCase @Inject constructor()
    : BaseUseCase<GetAvailableCurrenciesForSellUseCase.Param, List<Currency>> {

    override fun invoke(param: Param): List<Currency> {
        val (rates, currentBalance) = param

        val currenciesWithBalance = currentBalance.items
            .filter { it.amount > 0 }
            .map { it.currency }
            .toSet()

        return rates.rates.keys.filter { it in currenciesWithBalance }
    }

    data class Param(
        val rates: ExchangeRates,
        val currentBalance: Balances
    )
}