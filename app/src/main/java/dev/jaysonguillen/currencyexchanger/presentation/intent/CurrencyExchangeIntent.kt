package dev.jaysonguillen.currencyexchanger.presentation.intent

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange

sealed class CurrencyExchangerIntent {
    object StartPolling : CurrencyExchangerIntent()
    object StopPolling : CurrencyExchangerIntent()
    object FetchRates : CurrencyExchangerIntent()
    object FetchBalances : CurrencyExchangerIntent()
    object FetchCurrencyExchanges : CurrencyExchangerIntent()
    data class CalculateCommission(val amount: Double) : CurrencyExchangerIntent()
    data class SaveCurrencyExchange(val currencyExchange: CurrencyExchange) : CurrencyExchangerIntent()
    data class UpdateBalance(val currency: String, val newBalance: Double) : CurrencyExchangerIntent()
    object Refresh : CurrencyExchangerIntent()
}
