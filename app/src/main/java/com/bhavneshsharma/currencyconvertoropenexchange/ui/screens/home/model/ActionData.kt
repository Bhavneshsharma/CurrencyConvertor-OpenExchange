package com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.model

data class ActionData(
    val loadingStatus: Boolean = false,
    val message: String? = null,
    val successCurrencyData: List<CurrencyData> = emptyList(),
    val successCurrencyName : List<String> = emptyList()
)
