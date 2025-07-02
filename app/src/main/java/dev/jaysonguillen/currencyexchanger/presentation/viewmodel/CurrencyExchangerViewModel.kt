package dev.jaysonguillen.currencyexchanger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.domain.CurrencyExchangeUseCase
import dev.jaysonguillen.currencyexchanger.presentation.effect.UiEffect
import dev.jaysonguillen.currencyexchanger.presentation.intent.CurrencyExchangerIntent
import dev.jaysonguillen.currencyexchanger.presentation.state.CurrencyExchangerState
import dev.jaysonguillen.currencyexchanger.presentation.state.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect = _effect.asSharedFlow()

    private var pollingJob: Job? = null

    private val handler = CoroutineExceptionHandler { _, throwable ->
        _state.update { it.copy(isLoading = false, error = throwable.message) }
    }

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
        pollingJob = viewModelScope.launch(handler) {
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

    private fun fetchRates() = launchWithHandler {
        _state.update { it.copy(isLoading = true, error = null) }
        val response = currencyExchangeUseCase.getCurrencyExchangeRates()
        _state.update { it.copy(rates = response, isLoading = false) }
    }

    private fun fetchBalances() {
        viewModelScope.launch(handler) {
            currencyExchangeUseCase.getBalances()
                .catch { e ->
                    _state.update { it.copy(error = e.message) }
                }
                .collect { balances ->
                    _state.update { it.copy(balances = balances) }
                }
        }
    }

    private fun fetchCurrencyExchanges() {
        viewModelScope.launch(handler) {
            _state.update { it.copy(currencyExchanges = UiState.Loading) }

            currencyExchangeUseCase.getCurrencyExchanges()
                .catch { e ->
                    _state.update {
                        it.copy(currencyExchanges = UiState.Error(e.message ?: "Unknown error"))
                    }
                }
                .collect { result ->
                    _state.update {
                        it.copy(currencyExchanges = UiState.Success(result))
                    }
                }
        }
    }

    private fun calculateCommission(amount: Double) {
        val commission = currencyExchangeUseCase.calculateCommission(amount)
        _state.update { it.copy(commission = commission) }
    }

    private fun saveCurrencyExchange(exchange: CurrencyExchange) {
        viewModelScope.launch(handler) {
            currencyExchangeUseCase.saveCurrencyExchange(exchange)
            _effect.emit(UiEffect.ShowSuccessExchangeDialog)
        }
    }

    private fun updateBalance(currency: String, newBalance: Double) {
        viewModelScope.launch(handler) {
            currencyExchangeUseCase.updateBalance(currency, newBalance)
        }
    }

    private fun launchWithHandler(block: suspend () -> Unit) {
        viewModelScope.launch(handler) {
            block()
        }
    }
}




