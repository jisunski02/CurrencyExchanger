package dev.jaysonguillen.currencyexchanger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import dev.jaysonguillen.currencyexchanger.domain.CurrencyExchangeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyExchangerViewModel @Inject constructor(
    private val currencyExchangeUseCase: CurrencyExchangeUseCase
) : ViewModel() {

    private val _currencyRates = MutableStateFlow(ExchangeRatesResponse())
    val currencyRates: StateFlow<ExchangeRatesResponse> = _currencyRates.asStateFlow()

    private val _myBalances = MutableStateFlow<List<MyBalances>>(emptyList())
    val myBalances: StateFlow<List<MyBalances>> = _myBalances.asStateFlow()

    private val _currencyExchange = MutableStateFlow<List<CurrencyExchange>>(emptyList())
    val currencyExchange: StateFlow<List<CurrencyExchange>> = _currencyExchange.asStateFlow()

    private val _commission = MutableStateFlow(0.0)
    val commission: StateFlow<Double> = _commission.asStateFlow()

    private var pollingJob: Job? = null

    init {
        startPolling()
        fetchBalances()
        fetchCurrencyExchanges()
    }

    fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (isActive) {
                fetchRates()
                delay(5000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun fetchRates() {
        viewModelScope.launch {
            try {
                val response = currencyExchangeUseCase.getCurrencyExchangeRates()
                _currencyRates.value = response
                Log.d(TAG, "Success: ${response.rates}")

            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}", e)
            }
        }
    }

    fun fetchBalances(){
        viewModelScope.launch {
            try {
                currencyExchangeUseCase.getBalances().collect { myBalances->
                    _myBalances.value = myBalances
                }

            } catch (e: Exception){
                Log.e(TAG, "Error: ${e.message}", e)
            }

        }
    }

    fun fetchCurrencyExchanges(){
        viewModelScope.launch {
            try {
                currencyExchangeUseCase.getCurrencyExchanges().collect { currencyExchange->
                    _currencyExchange.value = currencyExchange
                }

            } catch (e: Exception){
                Log.e(TAG, "Error: ${e.message}", e)
            }

        }
    }

    fun calculateCommission(amount: Double){
        _commission.value = currencyExchangeUseCase.calculateCommission(amount)
        Log.d("InvokeHere", "$amount ${_commission.value}")
    }

    fun saveCurrencyExchange(currencyExchange: CurrencyExchange){
        viewModelScope.launch {
            currencyExchangeUseCase.saveCurrencyExchange(currencyExchange)
        }
    }

    fun updateBalance(currency: String, newBalance: Double){
        viewModelScope.launch {
            currencyExchangeUseCase.updateBalance(currency, newBalance)
        }
    }


    fun refresh() {
        viewModelScope.launch {
            fetchRates()
        }
    }

    companion object {
        const val TAG = "CurrencyViewModel"
    }
}


