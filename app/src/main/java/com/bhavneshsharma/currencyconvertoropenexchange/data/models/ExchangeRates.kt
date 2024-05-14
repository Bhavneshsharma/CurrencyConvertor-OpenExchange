package com.bhavneshsharma.currencyconvertoropenexchange.data.models

data class ExchangeRates(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
