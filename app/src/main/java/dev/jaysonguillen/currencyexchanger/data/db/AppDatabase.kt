package dev.jaysonguillen.currencyexchanger.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances

@Database(
    entities = [MyBalances::class, CurrencyExchange::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getMyBalancesDao(): MyBalancesDao
    abstract fun getCurrencyExchangeDao(): CurrencyExchangeDao
}