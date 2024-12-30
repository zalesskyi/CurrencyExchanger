package com.example.currencyexchanger

import com.example.domain.models.ExchangeRates
import com.example.domain.usecases.ApplyCommissionFeeUseCase
import com.example.domain.usecases.CalculateConversionAmountUseCase
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApplyCommissionFeeUseCaseTest {

    private lateinit var applyCommissionFeeUseCase: ApplyCommissionFeeUseCase

    private lateinit var calculateConversionAmountUseCase: CalculateConversionAmountUseCase

    @Before
    fun setUp() {
        calculateConversionAmountUseCase = mockk(relaxed = true)
        applyCommissionFeeUseCase = ApplyCommissionFeeUseCase(calculateConversionAmountUseCase)
    }

    @Test
    fun `should return free of charge values when operation count is within limit`() {
        val param = ApplyCommissionFeeUseCase.Param(
            amountInSellCurrency = 1000F,
            amountInReceiveCurrency = 950F,
            sellCurrency = "EUR",
            receiveCurrency = "USD",
            exchangeRates = mockExchangeRates()
        )

        val result = applyCommissionFeeUseCase(param)

        assertNull(result.commissionFeeInSellCurrency)
        assertEquals(1000F, result.amountInSellCurrencyWithCommission)
        assertEquals(950F, result.amountInReceiveCurrencyWithCommission)
    }

    @Test
    fun `should apply commission fee and calculate amounts when operation count exceeds limit`() {
        applyCommissionFeeUseCase.exchangeOperationsCounter = 6

        val param = ApplyCommissionFeeUseCase.Param(
            amountInSellCurrency = 1000F,
            amountInReceiveCurrency = 950F,
            sellCurrency = "EUR",
            receiveCurrency = "USD",
            exchangeRates = mockExchangeRates()
        )

        every {
            calculateConversionAmountUseCase.invoke(any())
        } returns 10F

        val result = applyCommissionFeeUseCase(param)

        val expectedCommissionFee = 1000F * ApplyCommissionFeeUseCase.COMMISSION_FEE
        val expectedAmountInReceiveCurrencyWithCommission = 950F - 10F

        assertEquals(expectedCommissionFee, result.commissionFeeInSellCurrency)
        assertEquals(1000F + expectedCommissionFee, result.amountInSellCurrencyWithCommission)
        assertEquals(expectedAmountInReceiveCurrencyWithCommission, result.amountInReceiveCurrencyWithCommission)
    }

    private fun mockExchangeRates(): ExchangeRates {
        return ExchangeRates("EUR", emptyMap())
    }
}
