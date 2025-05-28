package dev.jaysonguillen.currencyexchanger.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Blue

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String = "Currency Converter",
    style: TextStyle = TextStyle(
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontWeight = FontWeight.ExtraBold
    )
){
    Text(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color_Blue)
            .padding(12.dp),
        text = title,
        style = style
    )
}