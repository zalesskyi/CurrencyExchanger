package com.example.currencyexchanger

import com.example.domain.models.ExchangeRates
import com.example.domain.usecases.CalculateConversionAmountUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CalculateConversionAmountUseCaseTest {

    private lateinit var calculateConversionAmountUseCase: CalculateConversionAmountUseCase

    @Before
    fun setUp() {
        calculateConversionAmountUseCase = CalculateConversionAmountUseCase()
    }

    @Test
    fun `should return correct conversion amount when valid parameters are provided`() {
        val sellAmount = 1000F
        val sellCurrency = "USD"
        val receiveCurrency = "EUR"
        val exchangeRates = mockExchangeRates()

        val param = CalculateConversionAmountUseCase.Param(
            sellAmount = sellAmount,
            sellCurrency = sellCurrency,
            receiveCurrency = receiveCurrency,
            rates = exchangeRates
        )

        val result = calculateConversionAmountUseCase(param)

        val expectedResult = sellAmount * (exchangeRates.rates[receiveCurrency]!! / exchangeRates.rates[sellCurrency]!!)
        assertEquals(expectedResult, result)
    }

    private fun mockExchangeRates(): ExchangeRates {
        return ExchangeRates(
            "EUR",
            mapOf(
                "USD" to 1.0F,
                "EUR" to 0.85F,
                "GBP" to 0.75F
            )
        )
    }
}
