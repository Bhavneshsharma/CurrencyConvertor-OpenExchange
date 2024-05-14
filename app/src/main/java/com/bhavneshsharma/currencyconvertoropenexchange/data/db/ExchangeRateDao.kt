package com.bhavneshsharma.currencyconvertoropenexchange.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeRate: CurrencyExchangeRates)

    @Query("SELECT * FROM currency_exchange_rates")
    fun getExchangeRates(): List<CurrencyExchangeRates>
}