package com.bhavneshsharma.currencyconvertoropenexchange.data.network

import com.bhavneshsharma.currencyconvertoropenexchange.BuildConfig
import com.bhavneshsharma.currencyconvertoropenexchange.data.models.ExchangeRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("latest.json")
    suspend fun getLatestExchangeRates(
        @Query("app_id") appId: String = BuildConfig.APP_ID,
        @Query("base") base: String
    ): Response<ExchangeRates>
}