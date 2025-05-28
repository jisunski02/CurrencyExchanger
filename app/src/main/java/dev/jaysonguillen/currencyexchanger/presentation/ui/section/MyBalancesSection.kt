package dev.jaysonguillen.currencyexchanger.presentation.ui.section

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.data.model.MyBalances
import dev.jaysonguillen.currencyexchanger.presentation.ui.component.Label
import dev.jaysonguillen.currencyexchanger.presentation.ui.dialog.CurrencyExchangeDialog
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Green_2

@Composable
fun MyBalancesSection(
    balances: List<MyBalances>,
    currencyRates: Map<String, Double>,
    onSubmit: (CurrencyExchange) -> Unit = { }
    ){

    var showDialog by remember { mutableStateOf(false) }

    var currencyOrAmount by remember { mutableStateOf("" to 0.00) }

    Column(modifier = Modifier.padding(12.dp)) {
        Label()
        BalancesList(balances = balances,
            onCurrencyClick = { currency, amountBalance ->
                showDialog = true
                currencyOrAmount = currencyOrAmount.copy(currency, amountBalance)
            }
        )
    }

    if(showDialog){
        CurrencyExchangeDialog(
            currencyRates = currencyRates,
            currencyOrAmount = currencyOrAmount,
            onDismiss = { showDialog = false },
            onSubmit = { currencyExchange ->
                onSubmit(currencyExchange)
            }
        )
    }
}

@Composable
fun BalancesList(
    balances: List<MyBalances>,
    onCurrencyClick: (String, Double) -> Unit = {_,_->}
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(balances) { balance ->
            BalanceRowItem(
                balance = balance,
                onCurrencyClick = { currency, amountBalance ->
                    onCurrencyClick(currency, amountBalance)
                }
            )
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun BalanceRowItem(
    balance: MyBalances,
    onCurrencyClick: (String, Double) -> Unit = {_,_->}
    ) {
    Text(
        text = String.format("%.2f %s", balance.amountBalance, balance.currency),
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .background(
                color = if(balance.currency == "EUR") Color_Green_2 else Color.Transparent,
                shape = if(balance.currency == "EUR") RoundedCornerShape(12.dp) else RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable{
                if(balance.currency == "EUR"){
                    onCurrencyClick(balance.currency, balance.amountBalance)
                }
            },
        color = if(balance.currency == "EUR") Color.White else Color.Black

    )
}
