package com.example.domain.usecases

import com.example.domain.base.BaseUseCase
import com.example.domain.models.Currency
import com.example.domain.models.ExchangeRates
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Applies commission for transaction.
 * This use case is open for overriding.
 *
 * Within the current implementation the first 5 operations are free of charge.
 */
@Singleton
open class ApplyCommissionFeeUseCase @Inject constructor(
    private val calculateConversionAmountUseCase: CalculateConversionAmountUseCase
) : BaseUseCase<ApplyCommissionFeeUseCase.Param, ApplyCommissionFeeUseCase.Value> {

    @VisibleForTesting
    var exchangeOperationsCounter = 0

    override fun invoke(param: Param): Value {
        if (++exchangeOperationsCounter <= FREE_OF_CHARGE_OPERATIONS_COUNT) {
            return Value(
                commissionFeeInSellCurrency = null,
                amountInSellCurrencyWithCommission = param.amountInSellCurrency,
                amountInReceiveCurrencyWithCommission = param.amountInReceiveCurrency
            )
        }

        val (amountInSellCurrency, amountInReceiveCurrency, sellCurrency, receiveCurrency, rates) = param

        val commissionFeeInSellCurrency = amountInSellCurrency * COMMISSION_FEE
        val amountInReceiveCurrencyWithCommission = amountInReceiveCurrency -
                calculateConversionAmountUseCase(
                    CalculateConversionAmountUseCase.Param(
                        sellAmount = commissionFeeInSellCurrency,
                        sellCurrency = sellCurrency,
                        receiveCurrency = receiveCurrency,
                        rates = rates
                    )
                )
        val amountInSellCurrencyWithCommission = amountInSellCurrency + commissionFeeInSellCurrency
        return Value(
            commissionFeeInSellCurrency = commissionFeeInSellCurrency,
            amountInSellCurrencyWithCommission = amountInSellCurrencyWithCommission,
            amountInReceiveCurrencyWithCommission = amountInReceiveCurrencyWithCommission
        )
    }

    data class Param(
        val amountInSellCurrency: Float,
        val amountInReceiveCurrency: Float,
        val sellCurrency: Currency,
        val receiveCurrency: Currency,
        val exchangeRates: ExchangeRates
    )

    data class Value(
        val commissionFeeInSellCurrency: Float?,
        val amountInSellCurrencyWithCommission: Float,
        val amountInReceiveCurrencyWithCommission: Float
    )

    companion object {

        @VisibleForTesting
        internal const val COMMISSION_FEE = 0.007F

        @VisibleForTesting
        internal const val FREE_OF_CHARGE_OPERATIONS_COUNT = 5
    }
}