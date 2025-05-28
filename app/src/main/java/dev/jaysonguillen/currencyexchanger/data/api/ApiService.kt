package dev.jaysonguillen.currencyexchanger.data.api

import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.core.utils.Constants
import retrofit2.http.GET

interface ApiService {
    @GET(Constants.GET_CURRENCY_EXCHANGE_RATES)
    suspend fun getCurrencyExchangeRates(): ExchangeRatesResponse
}