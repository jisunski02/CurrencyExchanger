package dev.jaysonguillen.currencyexchanger.presentation.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.jaysonguillen.currencyexchanger.R
import dev.jaysonguillen.currencyexchanger.presentation.intent.CurrencyExchangerIntent
import dev.jaysonguillen.currencyexchanger.presentation.state.CurrencyExchangeUiState
import dev.jaysonguillen.currencyexchanger.presentation.ui.component.Header
import dev.jaysonguillen.currencyexchanger.presentation.ui.dialog.SuccessConversionDialog
import dev.jaysonguillen.currencyexchanger.presentation.ui.section.CurrencyExchangeSection
import dev.jaysonguillen.currencyexchanger.presentation.ui.section.MyBalancesSection
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Blue
import dev.jaysonguillen.currencyexchanger.presentation.viewmodel.CurrencyExchangerViewModel

@Preview(showBackground = true)
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyExchangerViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    var soldCurrency by remember { mutableStateOf("") }
    var receivedCurrency by remember { mutableStateOf("") }
    var soldAmount by remember { mutableDoubleStateOf(0.0) }
    var receivedAmount by remember { mutableDoubleStateOf(0.0) }
    var currentBalance by remember { mutableDoubleStateOf(0.0) }

    Log.d("currencyRates", "${state.rates.rates}")
    Log.d("commission", "${state.commission}")

    Column {
        Header()

        MyBalancesSection(
            balances = state.balances,
            currencyRates = state.rates.rates,
            onSubmit = { currencyExchange ->
                viewModel.processIntent(CurrencyExchangerIntent.CalculateCommission(currencyExchange.soldAmount))
                viewModel.processIntent(CurrencyExchangerIntent.SaveCurrencyExchange(currencyExchange))

                soldAmount = currencyExchange.soldAmount
                receivedAmount = currencyExchange.receivedAmount
                soldCurrency = currencyExchange.soldCurrency
                receivedCurrency = currencyExchange.receivedCurrency
                currentBalance = currencyExchange.currentBalance

                showDialog = true
            }
        )

        when (val exchangeState = state.currencyExchanges) {
            is CurrencyExchangeUiState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CurrencyExchangeUiState.Success -> {
                if (exchangeState.data.isNotEmpty()) {
                    CurrencyExchangeSection(exchangeState.data)
                } else {
                    NoListDataUi()
                }
            }

            is CurrencyExchangeUiState.Error -> {
                Text("Failed to load: ${exchangeState.message ?: "Unknown error"}")
            }
        }


        if (showDialog) {
            SuccessConversionDialog(
                soldAmount = soldAmount,
                receivedAmount = receivedAmount,
                soldCurrency = soldCurrency,
                receivedCurrency = receivedCurrency,
                commission = state.commission,
                onDismiss = {
                    showDialog = false
                    viewModel.processIntent(CurrencyExchangerIntent.UpdateBalance(soldCurrency, currentBalance - state.commission))
                    viewModel.processIntent(CurrencyExchangerIntent.UpdateBalance(receivedCurrency, receivedAmount))

                    Log.d("BalanceHere", "$currentBalance ${state.commission}")
                }
            )
        }

        DisposableEffect(viewModel) {
            onDispose {
                viewModel.processIntent(CurrencyExchangerIntent.StopPolling)
            }
        }
    }
}


@Composable
fun NoListDataUi(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No currency exchange yet")
        Spacer(Modifier.height(16.dp))
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(R.drawable.ic_currency_exchange),
            contentDescription = null,
            tint = Color_Blue
        )
    }

}