package dev.jaysonguillen.currencyexchanger.presentation.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.jaysonguillen.currencyexchanger.presentation.ui.theme.Color_Gray

@Composable
fun SuccessConversionDialog(
    soldAmount: Double,
    receivedAmount: Double,
    soldCurrency: String,
    receivedCurrency: String,
    commission: Double,
    onDismiss: () -> Unit = {},
){
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(color = Color.White, RoundedCornerShape(12.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Currency Converted",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "You have converted %.2f %s to %.2f %s. Commission fee is %.2f %s.".format(
                    soldAmount,
                    soldCurrency,
                    receivedAmount,
                    receivedCurrency,
                    commission,
                    soldCurrency
                ),
                style = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(Color_Gray)
            )

            Button(
                onClick = { onDismiss() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text("Done", color = Color.White)
            }
        }
    }
}