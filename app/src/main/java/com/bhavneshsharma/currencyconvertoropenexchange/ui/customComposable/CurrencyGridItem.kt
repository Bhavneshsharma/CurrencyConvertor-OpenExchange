package com.bhavneshsharma.currencyconvertoropenexchange.ui.customComposable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrencyGridItem(
    name: String,
    amount: String
) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        border = BorderStroke(1.5.dp, Color.Red)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (amount.isNotEmpty()) {
                Text(
                    text = amount,
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = Color.Green
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGridItemWithAmount() {
    CurrencyGridItem(name = "INR", amount = "1000")
}