package dev.jaysonguillen.currencyexchanger.data.repository.source

import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse

interface RemoteSource {
    suspend fun getCurrencyExchangeRates(): ExchangeRatesResponse
}