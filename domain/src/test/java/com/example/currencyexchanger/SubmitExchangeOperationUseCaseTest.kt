package com.example.currencyexchanger

import com.example.domain.models.Balance
import com.example.domain.models.Balances
import com.example.domain.repositories.BalanceRepository
import com.example.domain.usecases.SubmitExchangeOperationUseCase
import io.mockk.*
import org.junit.Before
import org.junit.Test

class SubmitExchangeOperationUseCaseTest {

    private lateinit var balanceRepository: BalanceRepository
    private lateinit var submitExchangeOperationUseCase: SubmitExchangeOperationUseCase

    @Before
    fun setUp() {
        balanceRepository = mockk(relaxed = true)
        submitExchangeOperationUseCase = SubmitExchangeOperationUseCase(balanceRepository)
    }

    @Test
    fun `should update balances correctly when both sell and receive currencies are present`() {
        val initialBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 100.0f),
                Balance(currency = "EUR", amount = 50.0f)
            )
        )
        val param = SubmitExchangeOperationUseCase.Param(
            currentBalance = initialBalances,
            sellCurrency = "USD",
            receiveCurrency = "EUR",
            sellAmountWithCommission = 10.0f,
            receiveAmountWithCommission = 9.0f
        )

        submitExchangeOperationUseCase(param)

        val expectedBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 90.0f), // 100 - 10
                Balance(currency = "EUR", amount = 59.0f)  // 50 + 9
            )
        )

        verify { balanceRepository.setCurrentBalance(expectedBalances) }
    }

    @Test
    fun `should create new balance for receive currency if not present`() {
        val initialBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 100.0f)
            )
        )
        val param = SubmitExchangeOperationUseCase.Param(
            currentBalance = initialBalances,
            sellCurrency = "USD",
            receiveCurrency = "EUR",
            sellAmountWithCommission = 10.0f,
            receiveAmountWithCommission = 9.0f
        )

        submitExchangeOperationUseCase(param)

        val expectedBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 90.0f), // 100 - 10
                Balance(currency = "EUR", amount = 9.0f)   // New balance
            )
        )

        verify { balanceRepository.setCurrentBalance(expectedBalances) }
    }

    @Test
    fun `should not modify balances other than sell and receive currencies`() {
        val initialBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 100.0f),
                Balance(currency = "EUR", amount = 50.0f),
                Balance(currency = "GBP", amount = 75.0f)
            )
        )
        val param = SubmitExchangeOperationUseCase.Param(
            currentBalance = initialBalances,
            sellCurrency = "USD",
            receiveCurrency = "EUR",
            sellAmountWithCommission = 10.0f,
            receiveAmountWithCommission = 9.0f
        )

        submitExchangeOperationUseCase(param)

        val expectedBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 90.0f), // 100 - 10
                Balance(currency = "EUR", amount = 59.0f), // 50 + 9
                Balance(currency = "GBP", amount = 75.0f)  // Unchanged
            )
        )

        verify { balanceRepository.setCurrentBalance(expectedBalances) }
    }

    @Test
    fun `should correctly invoke BalanceRepository`() {
        val initialBalances = Balances(
            items = listOf(
                Balance(currency = "USD", amount = 100.0f)
            )
        )
        val param = SubmitExchangeOperationUseCase.Param(
            currentBalance = initialBalances,
            sellCurrency = "USD",
            receiveCurrency = "EUR",
            sellAmountWithCommission = 10.0f,
            receiveAmountWithCommission = 9.0f
        )

        submitExchangeOperationUseCase(param)

        verify(exactly = 1) { balanceRepository.setCurrentBalance(any()) }
    }
}
