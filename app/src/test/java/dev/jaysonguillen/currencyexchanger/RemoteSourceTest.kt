package dev.jaysonguillen.currencyexchanger

import dev.jaysonguillen.currencyexchanger.data.api.ApiService
import dev.jaysonguillen.currencyexchanger.data.repository.source.RemoteSource
import dev.jaysonguillen.currencyexchanger.data.repository.source.RemoteSourceImpl
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RemoteSourceTest {

    private val apiService: ApiService = mockk(relaxed = true)

    private lateinit var remoteSource: RemoteSource

    @Before
    fun setUp() {
        remoteSource = RemoteSourceImpl(apiService)
    }

    @Test
    fun `verify getCurrencyExchangeRates is called`() = runBlocking {
        // Act
        remoteSource.getCurrencyExchangeRates()

        // Assert
        coVerify { apiService.getCurrencyExchangeRates() }
    }
}
