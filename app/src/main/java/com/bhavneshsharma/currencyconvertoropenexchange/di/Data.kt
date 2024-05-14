package com.bhavneshsharma.currencyconvertoropenexchange.di

import android.content.Context
import androidx.room.Room
import com.bhavneshsharma.currencyconvertoropenexchange.common.Constants
import com.bhavneshsharma.currencyconvertoropenexchange.data.db.ApplicationDatabase
import com.bhavneshsharma.currencyconvertoropenexchange.data.db.ExchangeRateDao
import com.bhavneshsharma.currencyconvertoropenexchange.data.network.APIService
import com.bhavneshsharma.currencyconvertoropenexchange.data.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object Data {

    @Provides
    fun provideApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            Constants.APPLICATION_DATABASE
        ).build()
    }

    @Provides
    fun provideAPIService(applicationDatabase: ApplicationDatabase): ExchangeRateDao {
        return applicationDatabase.exchangeRateDao
    }

    @Provides
    fun provideDataRepository(
        apiService: APIService,
        exchangeRateDao: ExchangeRateDao,
        @ApplicationContext context: Context
    ): DataRepository = DataRepository(apiService, exchangeRateDao, context)
}