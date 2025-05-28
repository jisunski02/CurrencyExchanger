package dev.jaysonguillen.currencyexchanger

import dev.jaysonguillen.currencyexchanger.data.db.AppDatabase
import dev.jaysonguillen.currencyexchanger.data.db.CurrencyExchangeDao
import dev.jaysonguillen.currencyexchanger.data.db.MyBalancesDao
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import dev.jaysonguillen.currencyexchanger.data.repository.source.LocalSourceImpl
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalSourceImplTest {

    private val myBalancesDao: MyBalancesDao = mockk(relaxed = true)
    private val currencyExchangeDao: CurrencyExchangeDao = mockk(relaxed = true)
    private val appDatabase: AppDatabase = mockk()

    private lateinit var localSource: LocalSourceImpl

    @Before
    fun setUp() {
        every { appDatabase.getMyBalancesDao() } returns myBalancesDao
        every { appDatabase.getCurrencyExchangeDao() } returns currencyExchangeDao

        localSource = LocalSourceImpl(appDatabase)
    }

    @Test
    fun `verify saveBalances is called`() = runBlocking {
        val balances = listOf(MyBalances(currency = "USD", amountBalance = 100.0))
        localSource.saveBalances(balances)
        coVerify { myBalancesDao.saveBalances(balances) }
    }

    @Test
    fun `verify updateBalance is called for EUR`() = runBlocking {
        localSource.updateBalance("EUR", 200.0)
        coVerify { myBalancesDao.updateBalance("EUR", 200.0) }
    }

    @Test
    fun `verify updateBalance is called for non-EUR`() = runBlocking {
        localSource.updateBalance("USD", 150.0)
        coVerify { myBalancesDao.addToBalance("USD", 150.0) }
    }

    @Test
    fun `verify getBalances is called`() {
        localSource.getBalances()
        verify { myBalancesDao.getBalances() }
    }

    @Test
    fun `verify saveCurrencyExchange is called`() = runBlocking {
        val currencyExchange = CurrencyExchange(0,100.0, "USD",110.0, "EUR", 0.007, "01-01-2025 14:22:05")
        localSource.saveCurrencyExchange(currencyExchange)
        coVerify { currencyExchangeDao.saveCurrencyExchange(currencyExchange) }
    }

    @Test
    fun `verify getCurrencyExchanges is called`() {
        localSource.getCurrencyExchanges()
        verify { currencyExchangeDao.getCurrencyExchanges() }
    }
}

