package dev.jaysonguillen.currencyexchanger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.jaysonguillen.currencyexchanger.presentation.ui.screen.CurrencyConverterScreen
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.CurrencyExchangerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyExchangerTheme {
                CurrencyConverterScreen()
            }
        }
    }
}



