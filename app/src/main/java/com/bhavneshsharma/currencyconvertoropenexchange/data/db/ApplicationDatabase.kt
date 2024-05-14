package com.bhavneshsharma.currencyconvertoropenexchange.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyExchangeRates::class],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract val exchangeRateDao: ExchangeRateDao
}