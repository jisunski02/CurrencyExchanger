package dev.jaysonguillen.currencyexchanger.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesResponse(
    val base: String = "USD",
    val date: String = "1970-01-01",
    val rates: Map<String, Double> = emptyMap()
)

