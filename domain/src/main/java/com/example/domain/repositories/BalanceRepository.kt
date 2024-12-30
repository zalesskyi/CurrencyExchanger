package com.example.domain.repositories

import com.example.domain.models.Balances
import kotlinx.coroutines.flow.Flow

interface BalanceRepository {

    fun getCurrentBalance(): Flow<Balances>

    fun setCurrentBalance(balances: Balances)
}