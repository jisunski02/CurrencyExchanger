package dev.jaysonguillen.currencyexchanger.presentation.ui.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.jaysonguillen.currencyexchanger.R
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.presentation.ui.component.Label
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Gray
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Green
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Green_2
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Red

@Composable
fun CurrencyExchangeSection(currencyExchanges: List<CurrencyExchange>){
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Label(label = "CURRENCY EXCHANGE")
        CurrencyExchangeList(currencyExchanges)
    }
}

@Composable
fun CurrencyExchangeList(currencyExchanges: List<CurrencyExchange>) {
    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, bottom = 50.dp)
    ){
        items(currencyExchanges) { currencyExchange ->
            CurrencyExchangeItem(currencyExchange)
        }
    }
}

@Composable
fun CurrencyExchangeItem(currencyExchange: CurrencyExchange) {
    Column {
        ExchangeRow(
            label = "Sell",
            amount = currencyExchange.soldAmount,
            currency = currencyExchange.soldCurrency,
            iconRes = R.drawable.ic_arrow_up,
            iconBg = Color_Red,
            amountColor = Color.Unspecified
        )

        Spacer(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(start = 52.dp, end = 12.dp)
                .background(Color_Gray)
        )

        ExchangeRow(
            label = "Receive",
            amount = currencyExchange.receivedAmount,
            currency = currencyExchange.receivedCurrency,
            iconRes = R.drawable.ic_arrow_down,
            iconBg = Color_Green,
            amountColor = Color_Green_2
        )
    }
}

@Composable
fun ExchangeRow(
    label: String,
    amount: Double,
    currency: String,
    iconRes: Int,
    iconBg: Color,
    amountColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(iconBg)
                    .padding(5.dp)
            )
            Text(text = label)
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (label == "Receive") "+${"%.2f".format(amount)}" else "%.2f".format(amount),
                color = amountColor
            )
            Spacer(Modifier.width(9.dp))
            Text(text = currency)
            Image(
                painter = painterResource(id = R.drawable.ic_dropdown),
                contentDescription = null
            )
        }
    }
}
