package dev.jaysonguillen.currencyexchanger.presentation.state

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances

data class CurrencyExchangerState(
    val rates: ExchangeRatesResponse = ExchangeRatesResponse(),
    val balances: List<MyBalances> = emptyList(),
    val currencyExchanges: UiState<List<CurrencyExchange>> = UiState.Loading,
    val commission: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
