package dev.jaysonguillen.currencyexchanger

import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import dev.jaysonguillen.currencyexchanger.data.repository.RepositoryImpl
import dev.jaysonguillen.currencyexchanger.data.repository.source.LocalSource
import dev.jaysonguillen.currencyexchanger.data.repository.source.RemoteSource
import dev.jaysonguillen.currencyexchanger.domain.repository.Repository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {

    private val remoteSource: RemoteSource = mockk(relaxed = true)
    private val localSource: LocalSource = mockk(relaxed = true)

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = RepositoryImpl(remoteSource, localSource)
    }

    @Test
    fun `verify getCurrencyExchangeRates is called`() = runBlocking {
        repository.getCurrencyExchangeRates()
        coVerify { remoteSource.getCurrencyExchangeRates() }
    }

    @Test
    fun `verify saveBalances is called`() = runBlocking {
        val balances = listOf(MyBalances(currency = "USD", amountBalance = 100.0))
        repository.saveBalances(balances)
        coVerify { localSource.saveBalances(balances) }
    }

    @Test
    fun `verify updateBalance is called`() = runBlocking {
        repository.updateBalance("USD", 50.0)
        coVerify { localSource.updateBalance("USD", 50.0) }
    }

    @Test
    fun `verify getBalances is called`() {
        repository.getBalances()
        verify { localSource.getBalances() }
    }

    @Test
    fun `verify saveCurrencyExchange is called`() = runBlocking {
        val exchange =  CurrencyExchange(0,100.0, "USD",110.0, "EUR", 0.007, "01-01-2025 14:22:05")
        repository.saveCurrencyExchange(exchange)
        coVerify { localSource.saveCurrencyExchange(exchange) }
    }

    @Test
    fun `verify getCurrencyExchanges is called`() {
        repository.getCurrencyExchanges()
        verify { localSource.getCurrencyExchanges() }
    }
}
