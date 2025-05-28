package dev.jaysonguillen.currencyexchanger.core.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PrefsManager @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun incrementTransactionCount() {
        val currentCount = prefs.getInt(TRANSACTION_COUNT, 0)
        prefs.edit { putInt(TRANSACTION_COUNT, currentCount + 1) }
    }

    fun getTransactionCount(): Int {
        return prefs.getInt(TRANSACTION_COUNT, 0)
    }

    fun clear() {
        prefs.edit { clear() }
    }

    companion object {
        const val TRANSACTION_COUNT = "transaction_count"
    }
}

