package com.example.currencyexchanger.ui.viewmodels

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.domain.models.Balances
import com.example.domain.models.Currency
import com.example.domain.models.ExchangeRates

class MainScreenContract {

    /**
     * Encapsulates the state of MainScreen.
     */
    sealed interface DisplayState {

        /**
         * The state that is present, while content is loading.
         */
        data object Loading : DisplayState

        /**
         * Represents the loaded content state.
         */
        @Immutable
        data class Loaded(
            val balances: Balances,
            val availableCurrenciesForCell: List<Currency>,
            val availableCurrenciesForReceive: List<Currency>,
            val sellAmount: String,
            val receiveAmount: String,
            val sellCurrency: Currency,
            val receiveCurrency: Currency,
            @StringRes val sellAmountError: Int?,
            val exchangeRates: ExchangeRates
        ) : DisplayState

        /**
         * Represents the error state.
         */
        data object Error : DisplayState
    }

    /**
     * The state of user selection.
     * This state update happens only on user events from UI.
     */
    data class UserSelectionState(
        val sellAmount: String,
        val receiveAmount: String,
        val sellCurrency: Currency,
        val receiveCurrency: Currency
    ) {

        companion object {

            fun default() = UserSelectionState(
                sellAmount = 0F.toString(),
                receiveAmount = 0F.toString(),
                sellCurrency = "EUR",
                receiveCurrency = "USD"
            )
        }
    }

    /**
     * Represents the error state for the the UI.
     *
     * @property sellAmountFieldErrorRes A string resource ID representing the error message for the sell amount field.
     */
    data class ErrorState(
        @StringRes val sellAmountFieldErrorRes: Int?
    )

    /**
     * Represents events that can occur in the main screen based on user interaction.
     */
    sealed class MainEvent {

        data class UpdateSellAmount(val amount: String) : MainEvent()

        data class UpdateSellCurrency(val currency: Currency) : MainEvent()

        data class UpdateReceiveCurrency(val currency: Currency) : MainEvent()

        data object Submit : MainEvent()
    }

    sealed interface MainSideEffect {

        data class CurrencyConverted(
            val sellAmount: String,
            val sellCurrency: Currency,
            val receiveAmount: String,
            val receiveCurrency: Currency,
            val commissionFee: String?,
            val commissionCurrency: Currency
        ) : MainSideEffect
    }
}