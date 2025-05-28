package dev.jaysonguillen.currencyexchanger.presentation.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Blue
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Gray


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrencyRates(
    currencyRates: Map<String, Double>,
    selectedCurrency: String? = null,
    onSelectCurrency: (currency: String, rate: Double) -> Unit = { _, _ -> }
) {
    val currencies = currencyRates.entries.toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .height(300.dp)
            .padding(8.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(currencies) { currency ->
            CurrencyItem(
                currency = currency.key,
                isSelected = selectedCurrency == currency.key,
                onClick = { onSelectCurrency(currency.key, currency.value) }
            )
        }
    }
}

@Composable
fun CurrencyItem(
    currency: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor = if (isSelected) Color_Blue else Color_Gray

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currency,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}