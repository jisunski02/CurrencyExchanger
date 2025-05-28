package dev.jaysonguillen.currencyexchanger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyExchange(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val soldAmount: Double = 0.0,
    val soldCurrency: String = "",
    val receivedAmount: Double = 0.0,
    val receivedCurrency: String = "",
    val conversionRate: Double = 0.0,
    val transactionDate: String = "",
    val currentBalance: Double = 0.0
)

val dummyCurrencyExchanges = listOf(
    CurrencyExchange(0, 100.0, "USD", 92.0, "EUR", conversionRate = 0.92, transactionDate = "2025-05-25"),
    CurrencyExchange(1, 200.0, "GBP", 255.0, "USD", conversionRate = 1.275, transactionDate = "2025-05-24"),
    CurrencyExchange(2, 1500.0, "JPY", 13.0, "AUD", conversionRate = 0.0087,  transactionDate = "2025-05-23"),
    CurrencyExchange(4, 500.0, "CAD", 370.0, "CHF", conversionRate = 0.74,  transactionDate = "2025-05-22"),
    CurrencyExchange(5, 50.0, "EUR", 54.5, "USD", conversionRate = 1.1,  transactionDate = "2025-05-21"),
    CurrencyExchange(6, 700.0, "INR", 7.7, "EUR", conversionRate = 0.011,  transactionDate = "2025-05-20"),
    CurrencyExchange(7, 20.0, "AUD", 13.2, "GBP", conversionRate = 0.66,  transactionDate = "2025-05-19"),
    CurrencyExchange(8, 1000.0, "PHP", 17.5, "SGD", conversionRate = 0.0175, transactionDate = "2025-05-18"),
    CurrencyExchange(9, 60.0, "CNY", 7.5, "EUR", conversionRate = 0.125,  transactionDate = "2025-05-17"),
    CurrencyExchange(10, 250.0, "ZAR", 13.6, "USD", conversionRate = 0.0545, transactionDate = "2025-05-16")
)

