package com.example.data.datasources

import com.example.domain.models.Balance
import com.example.domain.models.Balances
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BalanceLocalDataSource @Inject constructor() {

    private val balancesStateFlow = MutableStateFlow(defaultBalance())

    fun getBalance(): Flow<Balances> = balancesStateFlow

    fun setBalance(balances: Balances) {
        balancesStateFlow.update {
            balances
        }
    }

    private fun defaultBalance(): Balances =
        Balances(
            items = listOf(
                Balance(
                    currency = "EUR",
                    amount = 1000F
                )
            )
        )
}