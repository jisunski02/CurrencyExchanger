package dev.jaysonguillen.currencyexchanger.data.repository.source

import dev.jaysonguillen.currencyexchanger.data.db.AppDatabase
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSourceImpl @Inject constructor(private val appDatabase: AppDatabase): LocalSource {
    override suspend fun saveBalances(myBalances: List<MyBalances>)
        = appDatabase.getMyBalancesDao().saveBalances(myBalances)

    override suspend fun updateBalance(
        currency: String,
        newBalance: Double
    ): Int {
        return if (currency == "EUR") {
            appDatabase.getMyBalancesDao().updateBalance(currency, newBalance)
        } else {
            appDatabase.getMyBalancesDao().addToBalance(currency, newBalance)
        }
    }


    override fun getBalances(): Flow<List<MyBalances>>
        = appDatabase.getMyBalancesDao().getBalances()

    override suspend fun saveCurrencyExchange(currencyExchange: CurrencyExchange)
        = appDatabase.getCurrencyExchangeDao().saveCurrencyExchange(currencyExchange)

    override fun getCurrencyExchanges(): Flow<List<CurrencyExchange>>
        = appDatabase.getCurrencyExchangeDao().getCurrencyExchanges()
}