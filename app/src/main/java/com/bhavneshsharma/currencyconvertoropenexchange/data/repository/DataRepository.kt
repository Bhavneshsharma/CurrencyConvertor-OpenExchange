package com.bhavneshsharma.currencyconvertoropenexchange.data.repository

import android.content.Context
import com.bhavneshsharma.currencyconvertoropenexchange.common.Constants
import com.bhavneshsharma.currencyconvertoropenexchange.common.NetworkResult
import com.bhavneshsharma.currencyconvertoropenexchange.common.UtilityFunctions
import com.bhavneshsharma.currencyconvertoropenexchange.data.db.CurrencyExchangeRates
import com.bhavneshsharma.currencyconvertoropenexchange.data.db.ExchangeRateDao
import com.bhavneshsharma.currencyconvertoropenexchange.data.models.ExchangeRates
import com.bhavneshsharma.currencyconvertoropenexchange.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiService: APIService,
    private val exchangeRatesDao: ExchangeRateDao,
    private val context: Context
) {
    init {
        getExchangeRates()
    }

    fun getExchangeRates(): Flow<NetworkResult<List<CurrencyExchangeRates>>> =
        flow<NetworkResult<List<CurrencyExchangeRates>>> {
            emit(NetworkResult.Loading())
            val isNetworkAvailable = UtilityFunctions.isNetworkAvailable(context = context)

            if (isNetworkAvailable) {
                try {
                    // Fetch data from API
                    val response = apiService.getLatestExchangeRates(base = Constants.BASE_USD)
                    if (response.isSuccessful && response.body() != null) {
                        val exchangeRates = response.body()!!
                        // Store data in Room database
                        storeDataInDB(exchangeRates)
                        // Emit the fetched data
                        emit(NetworkResult.Success(makeSuccessData(exchangeRates)))
                    } else {
                        fetchDataFromDb()
                    }
                } catch (e: Exception) {
                    fetchDataFromDb()
                }
            } else {
                // Fetch data from Room database
                fetchDataFromDb()
            }
        }.flowOn(Dispatchers.IO)

    private suspend fun FlowCollector<NetworkResult<List<CurrencyExchangeRates>>>.fetchDataFromDb() {
        val exchangeRatesFromDb = exchangeRatesDao.getExchangeRates()
        if (exchangeRatesFromDb.isNotEmpty()) {
            emit(NetworkResult.Success(exchangeRatesFromDb))
        } else {
            emit(NetworkResult.Error(Constants.EMPTY_DATABASE_ERROR))
        }
    }

    private fun makeSuccessData(exchangeRates: ExchangeRates): MutableList<CurrencyExchangeRates> {
        val data: MutableList<CurrencyExchangeRates> =
            mutableListOf()
        exchangeRates.rates.forEach {
            data.add(
                CurrencyExchangeRates(
                    currencyName = it.key,
                    currencyRate = it.value
                )
            )
        }
        return data
    }

    private suspend fun storeDataInDB(exchangeRates: ExchangeRates) {
        exchangeRates.rates.forEach { (currency, rate) ->
            exchangeRatesDao.insertExchangeRate(
                CurrencyExchangeRates(
                    currency,
                    rate
                )
            )
        }
    }
}