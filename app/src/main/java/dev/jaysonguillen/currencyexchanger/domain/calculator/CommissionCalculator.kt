package dev.jaysonguillen.currencyexchanger.domain.calculator

import dev.jaysonguillen.currencyexchanger.domain.rules.CommissionRule

class CommissionCalculator(private val rules: List<CommissionRule>) {
    fun calculate(transactionIndex: Int, amount: Double): Double {
        return rules.firstNotNullOfOrNull { it.calculateCommission(transactionIndex, amount) } ?: 0.0
    }
}
