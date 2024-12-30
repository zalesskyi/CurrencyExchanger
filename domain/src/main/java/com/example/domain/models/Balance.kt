package com.example.domain.models

/**
 * Encapsulate the user balance [amount] for given [currency].
 */
data class Balance(val currency: Currency, val amount: Float)