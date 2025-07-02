package dev.jaysonguillen.currencyexchanger.presentation.state

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange

sealed class Result {
    data object Loading : Result()
    data class Success(val data: List<CurrencyExchange>) : Result()
    data class Error(val message: String?) : Result()
}


sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
