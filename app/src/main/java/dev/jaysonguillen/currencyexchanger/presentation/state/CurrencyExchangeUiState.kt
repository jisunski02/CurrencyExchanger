package dev.jaysonguillen.currencyexchanger.presentation.state

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange

sealed class CurrencyExchangeUiState {
    data object Loading : CurrencyExchangeUiState()
    data class Success(val data: List<CurrencyExchange>) : CurrencyExchangeUiState()
    data class Error(val message: String?) : CurrencyExchangeUiState()
}
