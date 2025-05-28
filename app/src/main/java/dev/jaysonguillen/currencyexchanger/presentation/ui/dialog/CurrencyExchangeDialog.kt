package dev.jaysonguillen.currencyexchanger.presentation.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.jaysonguillen.currencyexchanger.data.model.CurrencyExchange
import dev.jaysonguillen.currencyexchanger.presentation.ui.component.CurrencyRates
import dev.jaysonguillen.currencyexchanger.presentation.ui.component.Label
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrencyExchangeDialog(
    currencyRates: Map<String, Double> = emptyMap(),
    currencyOrAmount: Pair<String, Double>,
    onDismiss: () -> Unit = {},
    onSubmit: (CurrencyExchange) -> Unit = { }
) {
    var inputText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("") }
    var selectedRate by remember { mutableDoubleStateOf(0.00) }

    val typedAmount = inputText.toDoubleOrNull() ?: 0.0

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(color = Color.White, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "%.2f".format(
                        if (typedAmount in 0.0..currencyOrAmount.second)
                            currencyOrAmount.second - typedAmount
                        else
                            currencyOrAmount.second
                    ),
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = currencyOrAmount.first,
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                )
            }


            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
            }

            TextField(
                value = inputText,
                onValueChange = { newText ->
                    inputText = newText
                    val newAmount = newText.toDoubleOrNull()
                    errorMessage = when {
                        newAmount == null -> ""
                        newAmount > currencyOrAmount.second -> "You don’t have enough money"
                        newAmount <= 0.0 -> "Enter a valid amount"
                        else -> ""
                    }
                },
                placeholder = { Text("Enter amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    disabledContainerColor = Color(0xFFF0F0F0),
                    errorContainerColor = Color(0xFFF0F0F0),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            val convertedAmount = calculateConversion(typedAmount, selectedRate)

            if (typedAmount > 0 && selectedCurrency.isNotEmpty() && typedAmount in 0.0..currencyOrAmount.second) {
                Text(
                    text = "${"%.2f".format(typedAmount)} ${currencyOrAmount.first} to ${"%.2f".format(convertedAmount)} $selectedCurrency",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                )
            }

            CurrencyRates(
                currencyRates = currencyRates,
                selectedCurrency = if (selectedCurrency.isEmpty()) null else selectedCurrency,
                onSelectCurrency = { currency, rate ->
                    selectedCurrency = currency
                    selectedRate = rate
                }
            )

            Button(
                onClick = {
                    val newAmount = inputText.toDoubleOrNull()

                    errorMessage = when {
                        inputText.isBlank() -> "Please enter an amount"
                        newAmount == null -> "Amount must be a number"
                        newAmount > currencyOrAmount.second -> "You don’t have enough money"
                        newAmount <= 0.0 -> "Enter a valid amount"
                        selectedCurrency.isEmpty() -> "Please select a currency"
                        else -> ""
                    }

                    if (errorMessage.isEmpty()) {
                        onSubmit(CurrencyExchange(
                            soldAmount = typedAmount,
                            soldCurrency = "EUR",
                            receivedAmount = convertedAmount,
                            receivedCurrency = selectedCurrency,
                            conversionRate = 0.007,
                            transactionDate = getCurrentDateTime(),
                            currentBalance = currencyOrAmount.second - typedAmount
                        ))

                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Submit", color = Color.White)
            }

        }
    }
}

private fun calculateConversion(amount: Double, rate: Double): Double {
    return amount * rate
}

private fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date())
}


