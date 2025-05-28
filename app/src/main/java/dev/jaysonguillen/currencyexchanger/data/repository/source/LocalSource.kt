package dev.jaysonguillen.currencyexchanger.data.repository.source

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun saveBalances(myBalances: List<MyBalances>)
    suspend fun updateBalance(currency: String, newBalance: Double): Int
    fun getBalances(): Flow<List<MyBalances>>
    suspend fun saveCurrencyExchange(currencyExchange: CurrencyExchange)
    fun getCurrencyExchanges(): Flow<List<CurrencyExchange>>
}