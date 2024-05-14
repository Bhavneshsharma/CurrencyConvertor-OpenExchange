package com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.viewModel

import com.bhavneshsharma.currencyconvertoropenexchange.common.NetworkResult
import com.bhavneshsharma.currencyconvertoropenexchange.common.UtilityFunctions
import com.bhavneshsharma.currencyconvertoropenexchange.data.db.CurrencyExchangeRates
import com.bhavneshsharma.currencyconvertoropenexchange.data.repository.DataRepository
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.model.ActionData
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.model.CurrencyData
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: DataRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `test getData success`() = testDispatcher.run {
        // Given
        val mockExchangeRates = currencyExchangeRatesList()
        updateMockDataToActionStateFlow()

        // Then
        assert(
            viewModel.actionStateFlow.value == ActionData(
                loadingStatus = false,
                message = null,
                successCurrencyData = mockExchangeRates.map {
                    CurrencyData(
                        nameRate = it.currencyName to it.currencyRate,
                        amount = ""
                    )
                },
                successCurrencyName = listOf("USD", "EUR", "GBP")
            )
        )
    }

    @Test
    fun `test getData loading`() = testDispatcher.run {
        // Given
        coEvery { repository.getExchangeRates() } returns flow {
            emit(NetworkResult.Loading())
        }

        // When
        viewModel.getData()

        // Then
        assert(
            viewModel.actionStateFlow.value == ActionData(
                loadingStatus = true,
                message = null,
                successCurrencyData = emptyList(),
                successCurrencyName = emptyList()
            )
        )
    }

    @Test
    fun `test getData error`() = testDispatcher.run {
        // Given
        val errorMessage = "Error fetching data"
        coEvery { repository.getExchangeRates() } returns flow {
            emit(NetworkResult.Error(errorMessage))
        }

        // When
        viewModel.getData()

        // Then
        assert(
            viewModel.actionStateFlow.value == ActionData(
                loadingStatus = false,
                message = errorMessage,
                successCurrencyData = emptyList(),
                successCurrencyName = emptyList()
            )
        )
    }

    @Test
    fun `test updateCurrencyAmounts with empty amount`() = testDispatcher.run {
        // When
        viewModel.updateCurrencyAmounts("", "USD")

        // Then
        // Ensure no change in actionStateFlow
        assert(viewModel.actionStateFlow.value == viewModel.actionStateFlow.value)
    }

    @Test
    fun `test updateCurrencyAmounts with source currency not found`() = testDispatcher.run {
        // Given
        val mockExchangeRates = currencyExchangeRatesList()
        updateMockDataToActionStateFlow()
        val sourceCurrencyName = "CAD" // Currency not found list
        val amount = "1"

        // When
        viewModel.updateCurrencyAmounts(amount, sourceCurrencyName)

        // Then
        val expectedCurrencyData = mockExchangeRates.map {
            CurrencyData(
                nameRate = it.currencyName to it.currencyRate,
                amount = UtilityFunctions.decimalPoint(((amount.toDouble() / 1.0) * it.currencyRate).toString())
            )
        }
        // Ensure the amount is set to the input amount for all currencies
        assert(
            viewModel.actionStateFlow.value.successCurrencyData == expectedCurrencyData
        )
    }


    @Test
    fun `test updateCurrencyAmounts with source currency found`() = testDispatcher.run {
        // Given
        val mockExchangeRates = currencyExchangeRatesList()
        updateMockDataToActionStateFlow()
        val sourceCurrency = "USD"
        val amount = "10"
        val sourceCurrencyRate = 1.0

        // When
        viewModel.updateCurrencyAmounts(amount, sourceCurrency)

        // Then
        val expectedCurrencyData = mockExchangeRates.map {
            CurrencyData(
                nameRate = it.currencyName to it.currencyRate,
                amount = UtilityFunctions.decimalPoint(((amount.toDouble() / sourceCurrencyRate) * it.currencyRate).toString())
            )

        }
        assert(viewModel.actionStateFlow.value.successCurrencyData == expectedCurrencyData)
    }


    private fun currencyExchangeRatesList(): List<CurrencyExchangeRates> {
        val mockExchangeRates = listOf(
            CurrencyExchangeRates("USD", 1.0),
            CurrencyExchangeRates("EUR", 0.85),
            CurrencyExchangeRates("GBP", 0.75)
        )
        return mockExchangeRates
    }

    private fun updateMockDataToActionStateFlow() {
        val mockExchangeRates = currencyExchangeRatesList()
        coEvery { repository.getExchangeRates() } returns flow {
            emit(NetworkResult.Success(mockExchangeRates))
        }
        viewModel.getData()
    }
}
