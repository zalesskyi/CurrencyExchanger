package com.example.currencyexchanger.ui.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.example.currencyexchanger.R
import com.example.currencyexchanger.utils.formatAmountToString
import com.example.domain.models.Balances
import com.example.domain.models.Currency
import com.example.domain.models.ExchangeRates
import com.example.domain.repositories.BalanceRepository
import com.example.domain.usecases.ApplyCommissionFeeUseCase
import com.example.domain.usecases.CalculateConversionAmountUseCase
import com.example.domain.usecases.FetchExchangeRatesUseCase
import com.example.domain.usecases.GetAvailableCurrenciesForReceiveUseCase
import com.example.domain.usecases.GetAvailableCurrenciesForSellUseCase
import com.example.domain.usecases.SubmitExchangeOperationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val getAvailableCurrenciesForSellUseCase: GetAvailableCurrenciesForSellUseCase,
    private val getAvailableCurrenciesForReceiveUseCase: GetAvailableCurrenciesForReceiveUseCase,
    private val calculateConversionAmountUseCase: CalculateConversionAmountUseCase,
    private val applyCommissionFeeUseCase: ApplyCommissionFeeUseCase,
    private val submitExchangeOperationUseCase: SubmitExchangeOperationUseCase,
    fetchExchangeRatesUseCase: FetchExchangeRatesUseCase,
    balanceRepository: BalanceRepository
) : MainViewModel() {

    private val _userSelectionState = MutableStateFlow(
        MainScreenContract.UserSelectionState.default()
    )

    private val errorFlow = MutableStateFlow(MainScreenContract.ErrorState(null))

    private val _sideEffects =
        Channel<MainScreenContract.MainSideEffect>(capacity = Channel.CONFLATED)

    override val state = combine(
        fetchExchangeRatesUseCase(),
        balanceRepository.getCurrentBalance(),
        _userSelectionState,
        errorFlow,
        ::produceDisplayState
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainScreenContract.DisplayState.Loading
        )

    override val sideEffects: Flow<MainScreenContract.MainSideEffect> = _sideEffects.receiveAsFlow()

    override fun sendEvent(event: MainScreenContract.MainEvent) {
        when (event) {
            is MainScreenContract.MainEvent.UpdateSellAmount -> {
                onUpdateSellAmount(amount = event.amount)
            }

            is MainScreenContract.MainEvent.UpdateSellCurrency -> {
                onUpdateSellCurrency(currency = event.currency)
            }

            is MainScreenContract.MainEvent.UpdateReceiveCurrency -> {
                onUpdateReceiveCurrency(currency = event.currency)
            }

            is MainScreenContract.MainEvent.Submit -> {
                onCurrencyExchangeSubmit()
            }
        }
    }

    private fun produceDisplayState(
        exchangeRatesResult: Result<ExchangeRates>,
        currentBalance: Balances,
        currentUserSelection: MainScreenContract.UserSelectionState,
        errorState: MainScreenContract.ErrorState
    ): MainScreenContract.DisplayState {
        return MainScreenContract.DisplayState.Error
        return exchangeRatesResult.getOrNull()?.let { exchangeRates ->
            val receiveAmount = calculateConversionAmount(
                currentUserSelection = currentUserSelection,
                exchangeRates = exchangeRates
            )
            MainScreenContract.DisplayState.Loaded(
                balances = currentBalance,
                availableCurrenciesForCell = getAvailableCurrenciesForSellUseCase(
                    param = GetAvailableCurrenciesForSellUseCase.Param(
                        rates = exchangeRates,
                        currentBalance = currentBalance
                    )
                ),
                availableCurrenciesForReceive = getAvailableCurrenciesForReceiveUseCase(
                    param = GetAvailableCurrenciesForReceiveUseCase.Param(
                        rates = exchangeRates,
                        currencyToSell = currentUserSelection.sellCurrency,
                    )
                ),
                sellAmount = currentUserSelection.sellAmount,
                receiveAmount = receiveAmount.formatAmountToString(),
                sellCurrency = currentUserSelection.sellCurrency,
                receiveCurrency = currentUserSelection.receiveCurrency,
                sellAmountError = errorState.sellAmountFieldErrorRes,
                exchangeRates = exchangeRates
            )
        } ?: MainScreenContract.DisplayState.Error
    }

    private fun onUpdateSellAmount(amount: String) {
        _userSelectionState.update { state ->
            state.copy(sellAmount = amount.filterNot { it == '-' || it == ',' || it == ' ' })
        }
    }

    private fun onUpdateSellCurrency(currency: Currency) {
        (state.value as? MainScreenContract.DisplayState.Loaded)?.exchangeRates?.let { rates ->
            _userSelectionState.update { selectionState ->

                // Check if receive and new sell currencies are not equal.
                // If so, then update receive currency respectively.
                val newReceiveCurrency = if (currency == selectionState.receiveCurrency) {
                    rates.rates.keys.first { it != currency }
                } else {
                    selectionState.receiveCurrency
                }

                selectionState.copy(sellCurrency = currency, receiveCurrency = newReceiveCurrency)
            }
        }
    }

    private fun onUpdateReceiveCurrency(currency: Currency) {
        (state.value as? MainScreenContract.DisplayState.Loaded)?.exchangeRates?.let { rates ->
            _userSelectionState.update { selectionState ->

                // Check if new receive and sell currencies are not equal.
                // If so, then update sell currency respectively.
                val newSellCurrency = if (currency == selectionState.sellCurrency) {
                    rates.rates.keys.first { it != currency }
                } else {
                    selectionState.sellCurrency
                }

                selectionState.copy(receiveCurrency = currency, sellCurrency = newSellCurrency)
            }
        }
    }

    private fun onCurrencyExchangeSubmit() {
        (state.value as? MainScreenContract.DisplayState.Loaded)?.let { currentState ->
            val (
                commissionFeeInSellCurrency,
                amountInSellCurrencyWithCommission,
                amountInReceiveCurrencyWithCommission
            ) = applyCommissionFeeUseCase(
                ApplyCommissionFeeUseCase.Param(
                    amountInSellCurrency = currentState.sellAmount.toFloat(),
                    amountInReceiveCurrency = currentState.receiveAmount.toFloat(),
                    sellCurrency = currentState.sellCurrency,
                    receiveCurrency = currentState.receiveCurrency,
                    exchangeRates = currentState.exchangeRates
                )
            )

            if (!checkBalance(amountInSellCurrencyWithCommission, _userSelectionState.value, currentState.balances)) return

            submitExchangeOperationUseCase(
                SubmitExchangeOperationUseCase.Param(
                    currentBalance = currentState.balances,
                    sellCurrency = currentState.sellCurrency,
                    receiveCurrency = currentState.receiveCurrency,
                    sellAmountWithCommission = amountInSellCurrencyWithCommission,
                    receiveAmountWithCommission = amountInReceiveCurrencyWithCommission
                )
            )
            sendSideEffect(
                MainScreenContract.MainSideEffect.CurrencyConverted(
                    sellAmount = currentState.sellAmount,
                    sellCurrency = currentState.sellCurrency,
                    receiveAmount = amountInReceiveCurrencyWithCommission.formatAmountToString(),
                    receiveCurrency = currentState.receiveCurrency,
                    commissionFee = commissionFeeInSellCurrency?.formatAmountToString(),
                    commissionCurrency = currentState.sellCurrency
                )
            )
        }
    }

    private fun calculateConversionAmount(
        currentUserSelection: MainScreenContract.UserSelectionState,
        exchangeRates: ExchangeRates
    ): Float {
        return currentUserSelection.sellAmount.toFloatOrNull()?.let { sellAmount ->
            calculateConversionAmountUseCase(
                param = CalculateConversionAmountUseCase.Param(
                    sellAmount = sellAmount,
                    sellCurrency = currentUserSelection.sellCurrency,
                    receiveCurrency = currentUserSelection.receiveCurrency,
                    rates = exchangeRates
                )
            )
        } ?: 0F
    }

    private fun sendSideEffect(effect: MainScreenContract.MainSideEffect) {
        viewModelScope.launch {
            _sideEffects.send(effect)
        }
    }

    /**
     * Check if user has enough funds to proceed with transaction.
     * If not then [errorFlow] is updated with corresponding error.
     *
     * @return 'true' if user can proceed
     */
    private fun checkBalance(
        amountInSellCurrencyWithCommission: Float,
        currentUserSelection: MainScreenContract.UserSelectionState,
        currentBalance: Balances
    ): Boolean {
        val amountError = getAmountError(
            amountInSellCurrencyWithCommission = amountInSellCurrencyWithCommission,
            sellCurrency = currentUserSelection.sellCurrency,
            currentBalance = currentBalance
        )
        errorFlow.update {
            MainScreenContract.ErrorState(amountError)
        }
        return amountError == null
    }

    @StringRes
    private fun getAmountError(
        amountInSellCurrencyWithCommission: Float,
        sellCurrency: Currency,
        currentBalance: Balances
    ): Int? {
        currentBalance.items.find { it.currency == sellCurrency }?.amount?.let { currentAmount ->
            if (currentAmount < amountInSellCurrencyWithCommission) {
                return R.string.err_not_enough_funds
            }
        }
        return null
    }
}