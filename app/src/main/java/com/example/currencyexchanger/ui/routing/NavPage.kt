package com.example.currencyexchanger.ui.routing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class NavPage(val route: String): Parcelable {

    data object Main: NavPage("/main")
}