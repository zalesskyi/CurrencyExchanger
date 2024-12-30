package com.example.domain.models

/**
 * Encapsulates the data related to user balance.
 * Each item in [items] encapsulates the balance for single currency.
 */
data class Balances(val items: List<Balance>)