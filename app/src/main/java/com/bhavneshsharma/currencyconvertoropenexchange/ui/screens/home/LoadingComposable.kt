package com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bhavneshsharma.currencyconvertoropenexchange.common.Constants

@Preview(showBackground = true)
@Composable
fun LoadingComposable() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(200.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = Constants.DATA_LOADING, style = TextStyle(
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray
            )
        )
    }
}