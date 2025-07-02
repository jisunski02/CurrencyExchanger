package dev.jaysonguillen.currencyexchanger.domain.rules

// First Nth transactions are free
class FreeFirstTransactionsRule(private val freeCount: Int) : CommissionRule {
    override fun calculateCommission(transactionCount: Int, amount: Double): Double? {
        return if (transactionCount < freeCount) 0.0 else null
    }
}

// Every Nth transaction is free
class EveryNthFreeRule(private val nth: Int) : CommissionRule {
    override fun calculateCommission(transactionCount: Int, amount: Double): Double? {
        return if (transactionCount % nth == 0) 0.0 else null
    }
}

// Transactions under a certain amount are free
class FreeUnderAmountRule(private val threshold: Double) : CommissionRule {
    override fun calculateCommission(transactionCount: Int, amount: Double): Double? {
        return if (amount < threshold) 0.0 else null
    }
}

// Percentage commission
class PercentageCommissionRule(private val percentage: Double) : CommissionRule {
    override fun calculateCommission(transactionCount: Int, amount: Double): Double? {
        return amount * percentage
    }
}