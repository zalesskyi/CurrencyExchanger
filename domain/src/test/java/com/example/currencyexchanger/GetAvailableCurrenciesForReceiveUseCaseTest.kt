package com.example.currencyexchanger

import com.example.domain.models.ExchangeRates
import com.example.domain.usecases.GetAvailableCurrenciesForReceiveUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAvailableCurrenciesForReceiveUseCaseTest {

    private lateinit var getAvailableCurrenciesForReceiveUseCase: GetAvailableCurrenciesForReceiveUseCase

    @Before
    fun setUp() {
        getAvailableCurrenciesForReceiveUseCase = GetAvailableCurrenciesForReceiveUseCase()
    }

    @Test
    fun `should return currencies available for receive excluding the currency to sell`() {
        val rates = mockExchangeRates()
        val param = GetAvailableCurrenciesForReceiveUseCase.Param(
            rates = rates,
            currencyToSell = "USD"
        )

        val result = getAvailableCurrenciesForReceiveUseCase(param)

        assertEquals(2, result.size)
        assertTrue { result.contains("UAH") }
        assertTrue { result.contains("GBP") }
        assertTrue { !result.contains("USD") }
    }

    @Test
    fun `should return empty list when only the currency to sell is available`() {
        val rates = ExchangeRates("EUR", mapOf("USD" to 1.0f))
        val param = GetAvailableCurrenciesForReceiveUseCase.Param(
            rates = rates,
            currencyToSell = "USD"
        )

        val result = getAvailableCurrenciesForReceiveUseCase(param)

        assertEquals(0, result.size)
    }

    @Test
    fun `should return all currencies if the currency to sell is not in the rates list`() {
        val rates = mockExchangeRates()
        val param = GetAvailableCurrenciesForReceiveUseCase.Param(
            rates = rates,
            currencyToSell = "JPY"  // Currency not in the rates map
        )

        val result = getAvailableCurrenciesForReceiveUseCase(param)

        assertEquals(3, result.size)
        assertTrue { result.contains("USD") }
        assertTrue { result.contains("UAH") }
        assertTrue { result.contains("GBP") }
    }

    private fun mockExchangeRates(): ExchangeRates {
        return ExchangeRates(
            baseCurrency = "EUR",
            rates = mapOf(
                "USD" to 1.0f,
                "UAH" to 0.85f,
                "GBP" to 0.75f
            )
        )
    }
}
