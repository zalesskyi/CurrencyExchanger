package com.example.currencyexchanger.ui.screens.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyexchanger.R
import com.example.currencyexchanger.ui.theme.Green
import com.example.currencyexchanger.ui.viewmodels.MainScreenContract
import com.example.domain.models.Currency

@Composable
fun ReceiveSection(
    state: MainScreenContract.DisplayState.Loaded,
    onUpdateReceiveCurrency: (Currency) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = Green,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.receive), fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "+${state.receiveAmount}", fontSize = 16.sp, color = Green)
        Spacer(modifier = Modifier.width(8.dp))
        DropdownMenuField(items = state.availableCurrenciesForReceive,
            selectedItem = state.receiveCurrency,
            onItemSelected = onUpdateReceiveCurrency
        )
    }
}