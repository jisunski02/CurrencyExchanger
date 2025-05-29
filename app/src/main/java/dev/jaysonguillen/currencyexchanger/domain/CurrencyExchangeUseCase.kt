package dev.jaysonguillen.currencyexchanger.domain

import android.util.Log
import dev.jaysonguillen.currencyexchanger.core.utils.PrefsManager
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.ExchangeRatesResponse
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import dev.jaysonguillen.currencyexchanger.domain.calculator.CommissionCalculator
import dev.jaysonguillen.currencyexchanger.domain.repository.Repository
import dev.jaysonguillen.currencyexchanger.domain.rules.EveryNthFreeRule
import dev.jaysonguillen.currencyexchanger.domain.rules.FreeFirstTransactionsRule
import dev.jaysonguillen.currencyexchanger.domain.rules.FreeUnderAmountRule
import dev.jaysonguillen.currencyexchanger.domain.rules.PercentageCommissionRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CurrencyExchangeUseCase @Inject constructor(
    private val repository: Repository,
    private val prefsManager: PrefsManager
) {
    private val calculator = CommissionCalculator(
        listOf(
            FreeFirstTransactionsRule(5),
            EveryNthFreeRule(10),
            FreeUnderAmountRule(200.0),
            PercentageCommissionRule(0.007)
        )
    )

    fun calculateCommission(amount: Double): Double {
        val transactionCountBefore = prefsManager.getTransactionCount()

        val commission = calculator.calculate(transactionCountBefore, amount)
        prefsManager.incrementTransactionCount()

        return commission
    }

    suspend fun getCurrencyExchangeRates(): ExchangeRatesResponse
            = repository.getCurrencyExchangeRates()

    suspend fun getBalances(): Flow<List<MyBalances>> {
        val myBalances = repository.getBalances().first()

        if (myBalances.isEmpty()) {
            val rates = repository.getCurrencyExchangeRates().rates
            val initialBalances = rates.map { (currency, _) ->
                MyBalances(
                    currency = currency,
                    amountBalance = if (currency == "EUR") 1000.00 else 0.00
                )
            }
            repository.saveBalances(initialBalances)
        }

        return repository.getBalances()
    }

    suspend fun updateBalance(currency: String, newBalance: Double) : Int
            = repository.updateBalance(currency, newBalance)

    suspend fun saveCurrencyExchange(currencyExchange: CurrencyExchange)
            = repository.saveCurrencyExchange(currencyExchange)

    fun getCurrencyExchanges(): Flow<List<CurrencyExchange>>
            = repository.getCurrencyExchanges()
}