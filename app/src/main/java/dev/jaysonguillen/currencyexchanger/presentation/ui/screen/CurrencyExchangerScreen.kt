package dev.jaysonguillen.currencyexchanger.presentation.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.jaysonguillen.currencyexchanger.R
import dev.jaysonguillen.currencyexchanger.presentation.ui.component.Header
import dev.jaysonguillen.currencyexchanger.presentation.ui.dialog.SuccessConversionDialog
import dev.jaysonguillen.currencyexchanger.presentation.ui.section.CurrencyExchangeSection
import dev.jaysonguillen.currencyexchanger.presentation.ui.section.MyBalancesSection
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Blue
import dev.jaysonguillen.currencyexchanger.presentation.viewmodel.CurrencyExchangerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyExchangerViewModel = hiltViewModel()){

    val currencyRateState = viewModel.currencyRates.collectAsState()
    val myBalances = viewModel.myBalances.collectAsState()
    val commission = viewModel.commission.collectAsState()
    val currencyExchange = viewModel.currencyExchange.collectAsState()

    Log.d("currencyRates", "${currencyRateState.value.rates}")
    Log.d("commission", "${commission.value}")

    var showDialog by remember { mutableStateOf(false) }

    var soldCurrency by remember { mutableStateOf("") }
    var receivedCurrency by remember { mutableStateOf("") }
    var soldAmount by remember { mutableDoubleStateOf(0.0) }
    var receivedAmount by remember { mutableDoubleStateOf(0.0) }

    val scope = rememberCoroutineScope()

    Column{
        Header()
        MyBalancesSection(
            myBalances.value,
            currencyRateState.value.rates,
            onSubmit = { currencyExchange ->
                viewModel.calculateCommission(currencyExchange.soldAmount)
                viewModel.saveCurrencyExchange(currencyExchange)

                scope.launch {
                    delay(2000)
                    viewModel.updateBalance(currencyExchange.soldCurrency, currencyExchange.currentBalance - commission.value)
                }

                viewModel.updateBalance(currencyExchange.receivedCurrency, currencyExchange.receivedAmount)

                soldAmount = currencyExchange.soldAmount
                receivedAmount = currencyExchange.receivedAmount
                soldCurrency = currencyExchange.soldCurrency
                receivedCurrency = currencyExchange.receivedCurrency

                showDialog = true
            }
        )

        if(currencyExchange.value.isNotEmpty()){
            CurrencyExchangeSection(currencyExchange.value)
        } else {
            NoListDataUi()
        }

        if(showDialog){
            SuccessConversionDialog(
                soldAmount, receivedAmount,
                soldCurrency, receivedCurrency,
                commission.value,
                onDismiss = {
                    showDialog = false
                }
            )
        }


        DisposableEffect(viewModel) {
            onDispose {
                viewModel.stopPolling()
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