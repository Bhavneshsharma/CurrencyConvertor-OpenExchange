package com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bhavneshsharma.currencyconvertoropenexchange.common.Constants
import com.bhavneshsharma.currencyconvertoropenexchange.ui.customComposable.CurrencyGridItem
import com.bhavneshsharma.currencyconvertoropenexchange.ui.customComposable.CustomDropdownMenu
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.model.ActionData
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.viewModel.HomeViewModel

@Composable
fun HomeScreen() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    var selectedCurrency by remember {
        mutableStateOf("")
    }
    var amount by remember {
        mutableStateOf("")
    }
    homeViewModel.updateCurrencyAmounts(amount, selectedCurrency)
    val actionState by homeViewModel.actionStateFlow.collectAsState(ActionData())

    if (actionState.loadingStatus) {
        LoadingComposable()
    } else {
        if (actionState.message != null) {
            ErrorComposable(msg = actionState.message ?: Constants.SOMETHING_WENT_WRONG) {
                homeViewModel.getData()
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.LightGray
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {
                            amount = it
                        },
                        placeholder = {
                            Text(
                                text = Constants.ENTER_YOUR_AMOUNT, style = TextStyle(
                                    color = Color.LightGray
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.Red,
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black
                        ),
                        maxLines = 1,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                            autoCorrect = true
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        })
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .size(width = 150.dp, height = 50.dp)
                            .align(Alignment.End)
                            .heightIn(300.dp)
                    ) {
                        CustomDropdownMenu(
                            itemList = actionState.successCurrencyName,
                            itemSelected = Constants.BASE_USD
                        ) { selectedItem ->
                            selectedCurrency = selectedItem
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        items(actionState.successCurrencyData.size) { index ->
                            val currencyName = actionState.successCurrencyData[index].nameRate.first
                            val computeAmount = actionState.successCurrencyData[index].amount
                            CurrencyGridItem(name = currencyName, amount = computeAmount)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}