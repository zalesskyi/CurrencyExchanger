package com.example.currencyexchanger.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.currencyexchanger.R
import com.example.currencyexchanger.ui.routing.NavPage
import com.example.currencyexchanger.ui.theme.ExchangerTheme
import com.example.currencyexchanger.ui.viewmodels.MainScreenContract
import com.example.currencyexchanger.ui.viewmodels.MainViewModel
import com.example.domain.models.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.mainScreen(viewModel: MainViewModel) {
    composable(route = NavPage.Main.route) {
        val state by viewModel.state.collectAsStateWithLifecycle()
        MainScreen(
            state = state,
            sideEffect = viewModel.sideEffects,
            onUpdateSellCurrency = {
                viewModel.sendEvent(MainScreenContract.MainEvent.UpdateSellCurrency(it))
            },
            onUpdateSellAmount = {
                viewModel.sendEvent(MainScreenContract.MainEvent.UpdateSellAmount(it))
            },
            onUpdateReceiveCurrency = {
                viewModel.sendEvent(MainScreenContract.MainEvent.UpdateReceiveCurrency(it))
            },
            onSubmit = {
                viewModel.sendEvent(MainScreenContract.MainEvent.Submit)
            }
        )
    }
}

@Composable
fun MainScreen(
    state: MainScreenContract.DisplayState,
    sideEffect: Flow<MainScreenContract.MainSideEffect?>,
    onUpdateSellCurrency: (Currency) -> Unit,
    onUpdateSellAmount: (String) -> Unit,
    onUpdateReceiveCurrency: (Currency) -> Unit,
    onSubmit: () -> Unit
) {
    when (state) {
        is MainScreenContract.DisplayState.Loading -> MainScreenLoadingContent()
        is MainScreenContract.DisplayState.Loaded -> {
            MainScreenLoadedContent(
                state = state,
                onUpdateSellCurrency = onUpdateSellCurrency,
                onUpdateSellAmount = onUpdateSellAmount,
                onUpdateReceiveCurrency = onUpdateReceiveCurrency,
                onSubmit = onSubmit
            )
        }

        is MainScreenContract.DisplayState.Error -> MainScreenErrorContent()
    }
    SideEffectHandler(sideEffectFlow = sideEffect)
}

@Composable
private fun SideEffectHandler(sideEffectFlow: Flow<MainScreenContract.MainSideEffect?>) {
    var currencyConvertedEffect: MainScreenContract.MainSideEffect.CurrencyConverted? by remember {
        mutableStateOf(
            null
        )
    }
    LaunchedEffect(key1 = sideEffectFlow) {
        sideEffectFlow.collectLatest { effect ->
            when (effect) {
                is MainScreenContract.MainSideEffect.CurrencyConverted -> {
                    currencyConvertedEffect = effect
                }

                else -> Unit
            }
        }
    }
    currencyConvertedEffect?.let { effect ->
        AlertDialog(
            onDismissRequest = { currencyConvertedEffect = null },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = ExchangerTheme.colors.primary
                    ),
                    onClick = { currencyConvertedEffect = null }) {
                    Text(stringResource(R.string.done))
                }
            },
            title = { Text(text = stringResource(R.string.currency_converted_title)) },
            text = {
                val text = effect.commissionFee?.let {
                    stringResource(
                        R.string.currency_converted_with_commission,
                        effect.sellAmount,
                        effect.sellCurrency,
                        effect.receiveAmount,
                        effect.receiveCurrency,
                        effect.commissionFee,
                        effect.commissionCurrency
                    )
                } ?: stringResource(
                    R.string.currency_converted_desc,
                    effect.sellAmount,
                    effect.sellCurrency,
                    effect.receiveAmount,
                    effect.receiveCurrency,
                )
                Text(text = text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )
    }
}