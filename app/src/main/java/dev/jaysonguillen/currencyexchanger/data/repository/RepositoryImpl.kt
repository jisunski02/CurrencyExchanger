package dev.jaysonguillen.currencyexchanger.data.repository

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import dev.jaysonguillen.currencyexchanger.data.repository.source.LocalSource
import dev.jaysonguillen.currencyexchanger.data.repository.source.RemoteSource
import dev.jaysonguillen.currencyexchanger.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
): Repository {
    override suspend fun getCurrencyExchangeRates(): ExchangeRatesResponse
        = remoteSource.getCurrencyExchangeRates()

    override suspend fun saveBalances(myBalances: List<MyBalances>)
        = localSource.saveBalances(myBalances)

    override suspend fun updateBalance(
        currency: String,
        newBalance: Double
    ): Int
        = localSource.updateBalance(currency, newBalance)

    override fun getBalances(): Flow<List<MyBalances>>
        = localSource.getBalances()

    override suspend fun saveCurrencyExchange(currencyExchange: CurrencyExchange)
        = localSource.saveCurrencyExchange(currencyExchange)

    override fun getCurrencyExchanges(): Flow<List<CurrencyExchange>>
        = localSource.getCurrencyExchanges()
}
