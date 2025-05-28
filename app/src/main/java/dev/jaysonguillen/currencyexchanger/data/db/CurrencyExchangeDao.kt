package dev.jaysonguillen.currencyexchanger.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyExchangeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrencyExchange(currencyExchange: CurrencyExchange)

    @Query("SELECT * FROM CurrencyExchange")
    fun getCurrencyExchanges(): Flow<List<CurrencyExchange>>
}