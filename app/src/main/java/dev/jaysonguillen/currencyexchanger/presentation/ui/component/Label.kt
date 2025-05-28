package dev.jaysonguillen.currencyexchanger.presentation.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun Label(
    modifier: Modifier = Modifier,
    label: String = "MY BALANCES",
    style: TextStyle = TextStyle(
        color = Color.Gray,
        fontWeight = FontWeight.Bold
    )
    ){
    Text(
        modifier = modifier,
        text = label,
        style = style
    )
}