package com.bhavneshsharma.currencyconvertoropenexchange.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bhavneshsharma.currencyconvertoropenexchange.common.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class CurrencyExchangeRates(
    val currencyName: String,
    val currencyRate: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)