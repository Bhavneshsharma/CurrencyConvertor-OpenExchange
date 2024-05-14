package com.bhavneshsharma.currencyconvertoropenexchange.ui.navigation

import com.bhavneshsharma.currencyconvertoropenexchange.common.Constants

sealed class NavigationRouteItems(val route: String) {
    data object HomeScreen : NavigationRouteItems(route = Constants.HOME)
}