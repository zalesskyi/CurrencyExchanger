package com.example.domain.usecases

import com.example.domain.base.BaseUseCase
import com.example.domain.models.Balance
import com.example.domain.models.Balances
import com.example.domain.models.Currency
import com.example.domain.repositories.BalanceRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Update the balance based on sell
 * amount and receive amount (including commission).
 */
@Singleton
class SubmitExchangeOperationUseCase @Inject constructor(
    private val balanceRepository: BalanceRepository
) : BaseUseCase<SubmitExchangeOperationUseCase.Param, Unit> {

    override fun invoke(param: Param) {
        var (currentBalance, sellCurrency, receiveCurrency, sellAmount, receiveAmount) = param

        // check if we already have the balance for receiveCurrency
        // if not, create a new one
        if (!currentBalance.items.any { it.currency == receiveCurrency }) {
            currentBalance = currentBalance.copy(
                items = currentBalance.items + listOf(Balance(receiveCurrency, 0.0F))
            )
        }

        val newBalance = currentBalance.copy(
            items = currentBalance.items.map { balance ->
                return@map when (balance.currency) {
                    sellCurrency -> balance.copy(amount = balance.amount - sellAmount)
                    receiveCurrency -> balance.copy(amount = balance.amount + receiveAmount)
                    else -> balance
                }
            }
        )
        balanceRepository.setCurrentBalance(newBalance)
    }

    data class Param(
        val currentBalance: Balances,
        val sellCurrency: Currency,
        val receiveCurrency: Currency,
        val sellAmountWithCommission: Float,
        val receiveAmountWithCommission: Float
    )
}