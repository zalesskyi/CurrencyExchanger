package com.example.currencyexchanger.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class MainViewModel : ViewModel() {

    abstract val state: StateFlow<MainScreenContract.DisplayState>

    abstract val sideEffects: Flow<MainScreenContract.MainSideEffect>

    abstract fun sendEvent(event: MainScreenContract.MainEvent)
}