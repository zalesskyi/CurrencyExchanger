package com.example.data.repositories

import com.example.data.datasources.BalanceLocalDataSource
import com.example.domain.models.Balances
import com.example.domain.repositories.BalanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BalanceRepositoryImpl @Inject constructor(
    private val balanceDataSource: BalanceLocalDataSource
) : BalanceRepository {

    override fun getCurrentBalance(): Flow<Balances> {
        return balanceDataSource.getBalance()
    }

    override fun setCurrentBalance(balances: Balances) {
        balanceDataSource.setBalance(balances)
    }
}