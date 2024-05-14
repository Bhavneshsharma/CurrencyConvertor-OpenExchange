package com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhavneshsharma.currencyconvertoropenexchange.common.NetworkResult
import com.bhavneshsharma.currencyconvertoropenexchange.common.UtilityFunctions
import com.bhavneshsharma.currencyconvertoropenexchange.data.db.CurrencyExchangeRates
import com.bhavneshsharma.currencyconvertoropenexchange.data.repository.DataRepository
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.model.ActionData
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.model.CurrencyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    init {
        getData()
    }

    private val _actionStateFlow = MutableStateFlow(ActionData())
    val actionStateFlow: StateFlow<ActionData> = _actionStateFlow

    fun getData() {
        viewModelScope.launch {
            repository.getExchangeRates().collect { result ->
                when (result) {
                    is NetworkResult.Error -> handelErrorCase(result.message)
                    is NetworkResult.Loading -> handelLoadingCase()
                    is NetworkResult.Success -> handelSuccessCase(result.data)
                }
            }
        }
    }

    private fun handelSuccessCase(data: List<CurrencyExchangeRates>?) {
        val nameList: List<String> = data?.map { it.currencyName } ?: emptyList()
        val currencyDataList: List<CurrencyData>? = data?.map {
            CurrencyData(
                nameRate = it.currencyName to it.currencyRate,
                amount = ""
            )
        }
        _actionStateFlow.value = ActionData(
            loadingStatus = false,
            message = null,
            successCurrencyData = currencyDataList ?: emptyList(),
            successCurrencyName = nameList
        )
    }

    private fun handelLoadingCase() {
        _actionStateFlow.value = ActionData(
            loadingStatus = true,
            message = null,
            successCurrencyData = emptyList(),
            successCurrencyName = emptyList()
        )
    }

    private fun handelErrorCase(message: String?) {
        _actionStateFlow.value = ActionData(
            loadingStatus = false,
            message = message,
            successCurrencyData = emptyList(),
            successCurrencyName = emptyList()
        )
    }

    fun updateCurrencyAmounts(amount: String, sourceCurrencyName: String) {
        if (amount.isEmpty()) {
            return
        }
        val sourceCurrencyRate = _actionStateFlow.value.successCurrencyData
            .firstOrNull { it.nameRate.first == sourceCurrencyName }
            ?.nameRate?.second ?: 1.0 // Default to 1 if source currency not found

        _actionStateFlow.value = _actionStateFlow.value.copy(
            successCurrencyData = _actionStateFlow.value.successCurrencyData.map { currencyData ->
                val amountInSourceCurrency = amount.toDouble() / sourceCurrencyRate
                val amountInTargetCurrency = amountInSourceCurrency * currencyData.nameRate.second
                currencyData.copy(amount = UtilityFunctions.decimalPoint(amountInTargetCurrency.toString()))
            }
        )
    }
}