package com.example.domain.usecases

import com.example.domain.base.BaseUseCase
import com.example.domain.models.Currency
import com.example.domain.models.ExchangeRates
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Calculates the amount to receive based on exchange rate.
 */
@Singleton
class CalculateConversionAmountUseCase @Inject constructor()
    : BaseUseCase<CalculateConversionAmountUseCase.Param, Float> {

    override fun invoke(param: Param): Float {
        val (sellAmount, sellCurrency, receiveCurrency, rates) = param
        val sellCurrencyRate = rates.rates.getValue(sellCurrency)
        val receiveCurrencyRate = rates.rates.getValue(receiveCurrency)
        val baseRate = receiveCurrencyRate / sellCurrencyRate
        return sellAmount * baseRate
    }

    data class Param(
        val sellAmount: Float,
        val sellCurrency: Currency,
        val receiveCurrency: Currency,
        val rates: ExchangeRates
    )
}