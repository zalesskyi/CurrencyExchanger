package com.example.currencyexchanger.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyexchanger.R
import com.example.currencyexchanger.utils.formatAmountToString
import com.example.domain.models.Balance
import com.example.domain.models.Balances

@Composable
fun MyBalanceSection(balance: Balances) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(R.string.my_balances),
        fontSize = 16.sp,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between chips
    ) {
        items(balance.items) { item ->
            BalanceChip(
                balance = item
            )
        }
    }
}

@Composable
private fun BalanceChip(
    balance: Balance
) {
    Surface(
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceBright,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shadowElevation = 4.dp
    ) {
        Text(
            text = "${balance.amount.formatAmountToString()} ${balance.currency}",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}