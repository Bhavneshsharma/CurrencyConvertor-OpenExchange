package com.bhavneshsharma.currencyconvertoropenexchange.ui.customComposable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomDropdownMenu(
    itemList: List<String>,
    itemSelected: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(itemSelected) }

    OutlinedTextField(
        value = selectedItem,
        onValueChange = { selectedItem = it },
        readOnly = true,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { expanded = true }),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
        ),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Black,
            unfocusedContainerColor = Color.Black,
            unfocusedBorderColor = Color.Red,
            focusedBorderColor = Color.Red,
        ),
        trailingIcon = {
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable(onClick = { expanded = !expanded })
            )
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        Box(modifier = Modifier.size(width = 100.dp, height = 300.dp)) {
            LazyColumn {
                items(itemList.size) { index ->
                    val item = itemList[index]
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedItem = item
                            onItemSelected(item)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}