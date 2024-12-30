package com.example.currencyexchanger.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyexchanger.R
import com.example.currencyexchanger.ui.common.TopBar
import com.example.currencyexchanger.ui.theme.ExchangerTheme
import com.example.currencyexchanger.ui.viewmodels.MainScreenContract
import com.example.domain.models.Currency

@Composable
fun MainScreenLoadedContent(
    state: MainScreenContract.DisplayState.Loaded,
    onUpdateSellCurrency: (Currency) -> Unit,
    onUpdateSellAmount: (String) -> Unit,
    onUpdateReceiveCurrency: (Currency) -> Unit,
    onSubmit: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .imePadding(),
                colors = ButtonDefaults.buttonColors(containerColor = ExchangerTheme.colors.primary)
            ) {
                Text(text = stringResource(R.string.submit), color = Color.White)
            }
        },
        topBar = {
            TopBar(
                name = stringResource(R.string.currency_converter)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp)
        ) {
            MyBalanceSection(balance = state.balances)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.currency_exchange),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                SellSection(
                    state = state,
                    onUpdateSellAmount = onUpdateSellAmount,
                    onUpdateSellCurrency = onUpdateSellCurrency
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                ReceiveSection(
                    state = state,
                    onUpdateReceiveCurrency = onUpdateReceiveCurrency
                )
            }
        }
    }
}