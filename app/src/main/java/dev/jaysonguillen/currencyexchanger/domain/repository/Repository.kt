package dev.jaysonguillen.currencyexchanger.domain.repository

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCurrencyExchangeRates(): ExchangeRatesResponse
    suspend fun saveBalances(myBalances: List<MyBalances>)
    suspend fun updateBalance(currency: String, newBalance: Double): Int
    fun getBalances(): Flow<List<MyBalances>>
    suspend fun saveCurrencyExchange(currencyExchange: CurrencyExchange)
    fun getCurrencyExchanges(): Flow<List<CurrencyExchange>>
}