package dev.jaysonguillen.currencyexchanger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.domain.CurrencyExchangeUseCase
import dev.jaysonguillen.currencyexchanger.presentation.intent.CurrencyExchangerIntent
import dev.jaysonguillen.currencyexchanger.presentation.state.CurrencyExchangeUiState
import dev.jaysonguillen.currencyexchanger.presentation.state.CurrencyExchangerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyExchangerViewModel @Inject constructor(
    private val currencyExchangeUseCase: CurrencyExchangeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CurrencyExchangerState())
    val state: StateFlow<CurrencyExchangerState> = _state.asStateFlow()

    private var pollingJob: Job? = null

    init {
        processIntent(CurrencyExchangerIntent.StartPolling)
        processIntent(CurrencyExchangerIntent.FetchBalances)
        processIntent(CurrencyExchangerIntent.FetchCurrencyExchanges)
    }

    fun processIntent(event: CurrencyExchangerIntent) {
        when (event) {
            is CurrencyExchangerIntent.StartPolling -> startPolling()
            is CurrencyExchangerIntent.StopPolling -> stopPolling()
            is CurrencyExchangerIntent.FetchRates -> fetchRates()
            is CurrencyExchangerIntent.FetchBalances -> fetchBalances()
            is CurrencyExchangerIntent.FetchCurrencyExchanges -> fetchCurrencyExchanges()
            is CurrencyExchangerIntent.CalculateCommission -> calculateCommission(event.amount)
            is CurrencyExchangerIntent.SaveCurrencyExchange -> saveCurrencyExchange(event.currencyExchange)
            is CurrencyExchangerIntent.UpdateBalance -> updateBalance(event.currency, event.newBalance)
            is CurrencyExchangerIntent.Refresh -> fetchRates()
        }
    }

    private fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (isActive) {
                fetchRates()
                delay(5000)
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private fun fetchRates() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                val response = currencyExchangeUseCase.getCurrencyExchangeRates()
                _state.update { it.copy(rates = response, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun fetchBalances() {
        viewModelScope.launch {
            try {
                currencyExchangeUseCase.getBalances().collect { balances ->
                    _state.update { it.copy(balances = balances) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun fetchCurrencyExchanges() {
        viewModelScope.launch {
            _state.update { it.copy(currencyExchanges = CurrencyExchangeUiState.Loading) }

            try {
                currencyExchangeUseCase.getCurrencyExchanges().collect { result ->
                    _state.update {
                        it.copy(currencyExchanges = CurrencyExchangeUiState.Success(result))
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(currencyExchanges = CurrencyExchangeUiState.Error(e.message))
                }
            }
        }
    }

    private fun calculateCommission(amount: Double) {
        val commission = currencyExchangeUseCase.calculateCommission(amount)
        _state.update { it.copy(commission = commission) }
    }

    private fun saveCurrencyExchange(exchange: CurrencyExchange) {
        viewModelScope.launch {
            currencyExchangeUseCase.saveCurrencyExchange(exchange)
        }
    }

    private fun updateBalance(currency: String, newBalance: Double) {
        viewModelScope.launch {
            currencyExchangeUseCase.updateBalance(currency, newBalance)
        }
    }
}



