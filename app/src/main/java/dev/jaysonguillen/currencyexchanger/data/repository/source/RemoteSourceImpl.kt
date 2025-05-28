package dev.jaysonguillen.currencyexchanger.data.repository.source

import dev.jaysonguillen.currencyexchanger.data.api.ApiService
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import javax.inject.Inject

class RemoteSourceImpl @Inject constructor(
    private val apiService: ApiService
): RemoteSource {
    override suspend fun getCurrencyExchangeRates(): ExchangeRatesResponse
    = apiService.getCurrencyExchangeRates()
}