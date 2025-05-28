package dev.jaysonguillen.currencyexchanger.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import kotlinx.coroutines.flow.Flow

@Dao
interface MyBalancesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBalances(myBalances: List<MyBalances>)

    @Query("UPDATE MyBalances SET amountBalance = :newBalance WHERE currency = :currency")
    suspend fun updateBalance(currency: String, newBalance: Double): Int

    @Query("UPDATE MyBalances SET amountBalance = amountBalance + :amount WHERE currency = :currency")
    suspend fun addToBalance(currency: String, amount: Double): Int


    @Query("""
    SELECT * FROM MyBalances
    ORDER BY 
        CASE WHEN currency = 'EUR' THEN 0 ELSE 1 END,
        amountBalance DESC
""")
    fun getBalances(): Flow<List<MyBalances>>
}