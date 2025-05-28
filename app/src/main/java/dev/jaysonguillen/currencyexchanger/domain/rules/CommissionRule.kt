package dev.jaysonguillen.currencyexchanger.domain.rules

interface CommissionRule {
    fun calculateCommission(transactionCount: Int, amount: Double): Double?
}