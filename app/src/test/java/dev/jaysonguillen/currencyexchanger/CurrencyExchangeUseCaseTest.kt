package dev.jaysonguillen.currencyexchanger

import android.content.SharedPreferences
import dev.jaysonguillen.currencyexchanger.core.utils.PrefsManager
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.domain.CurrencyExchangeUseCase
import dev.jaysonguillen.currencyexchanger.domain.repository.Repository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyExchangeUseCaseTest {

    private lateinit var useCase: CurrencyExchangeUseCase
    private val repository: Repository = mockk(relaxed = true)
    private val prefsManager: PrefsManager = mockk()

    @Before
    fun setUp() {
        useCase = CurrencyExchangeUseCase(repository, prefsManager)
    }

    @Test
    fun `verify getCurrencyExchangeRates is called`() = runBlocking {
        useCase.getCurrencyExchangeRates()
        coVerify { repository.getCurrencyExchangeRates() }
    }

    @Test
    fun `verify getBalances is called`() = runBlocking {
        coEvery { repository.getBalances() } returns flowOf(emptyList())
        coEvery { repository.getCurrencyExchangeRates() } returns ExchangeRatesResponse(rates = emptyMap())
        coEvery { repository.saveBalances(any()) } just Runs

        useCase.getBalances()
        coVerify { repository.getBalances() }
    }

    @Test
    fun `verify updateBalance is called`() = runBlocking {
        useCase.updateBalance("USD", 500.0)
        coVerify { repository.updateBalance("USD", 500.0) }
    }

    @Test
    fun `verify saveCurrencyExchange is called`() = runBlocking {
        val exchange = CurrencyExchange(0,100.0, "USD",110.0, "EUR", 0.007, "01-01-2025 14:22:05")
        useCase.saveCurrencyExchange(exchange)
        coVerify { repository.saveCurrencyExchange(exchange) }
    }

    @Test
    fun `verify getCurrencyExchanges is called`() {
        every { repository.getCurrencyExchanges() } returns flowOf(emptyList())
        useCase.getCurrencyExchanges()
        verify { repository.getCurrencyExchanges() }
    }

    @Test
    fun `verify calculateCommission calls prefs methods`() {
        // Arrange
        every { prefsManager.getTransactionCount() } returns 0
        every { prefsManager.incrementTransactionCount() } just Runs

        // Act
        useCase.calculateCommission(100.0)

        // Assert
        verify(exactly = 1) { prefsManager.getTransactionCount() }
        verify(exactly = 1) { prefsManager.incrementTransactionCount() }
    }
}
