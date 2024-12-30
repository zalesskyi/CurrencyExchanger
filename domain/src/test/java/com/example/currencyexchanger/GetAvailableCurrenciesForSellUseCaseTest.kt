package com.example.currencyexchanger

import com.example.domain.models.Balance
import com.example.domain.models.Balances
import com.example.domain.models.ExchangeRates
import com.example.domain.usecases.GetAvailableCurrenciesForSellUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAvailableCurrenciesForSellUseCaseTest {

    private lateinit var getAvailableCurrenciesForSellUseCase: GetAvailableCurrenciesForSellUseCase

    @Before
    fun setUp() {
        getAvailableCurrenciesForSellUseCase = GetAvailableCurrenciesForSellUseCase()
    }

    @Test
    fun `should return currencies with balance that exist in exchange rates`() {
        val param = GetAvailableCurrenciesForSellUseCase.Param(
            rates = mockExchangeRates(),
            currentBalance = mockBalances()
        )

        val result = getAvailableCurrenciesForSellUseCase(param)

       assertEquals(2, result.size)
        assertTrue { result.contains("USD") }
        assertTrue { result.contains("EUR") }
        assertTrue { !result.contains("GBP") } // GBP has no balance
        assertTrue { !result.contains("JPY") } // JPY is not in exchange rates
    }

    @Test
    fun `should return empty list when no balance items have positive amounts`() {
        val param = GetAvailableCurrenciesForSellUseCase.Param(
            rates = mockExchangeRates(),
            currentBalance = Balances(
                items = listOf(
                    Balance(currency = "USD", amount = 0f),
                    Balance(currency = "EUR", amount = 0f)
                )
            )
        )

        val result = getAvailableCurrenciesForSellUseCase(param)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return empty list when no rates match currencies with balance`() {
        val param = GetAvailableCurrenciesForSellUseCase.Param(
            rates = ExchangeRates(baseCurrency = "EUR", rates = mapOf("GBP" to 0.75f)),
            currentBalance = mockBalances()
        )

        val result = getAvailableCurrenciesForSellUseCase(param)

        assertTrue(result.isEmpty())
    }

    private fun mockExchangeRates(): ExchangeRates {
        return ExchangeRates(
            baseCurrency = "EUR",
            rates = mapOf(
                "USD" to 1.0f,
                "EUR" to 0.85f,
                "GBP" to 0.75f
            )
        )
    }

    private fun mockBalances(): Balances {
        return Balances(
            items = listOf(
                Balance(currency = "USD", amount = 100f),
                Balance(currency = "EUR", amount = 50f),
                Balance(currency = "GBP", amount = 0f),
                Balance(currency = "JPY", amount = 200f) // JPY not in exchange rates
            )
        )
    }
}
