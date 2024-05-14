package com.bhavneshsharma.currencyconvertoropenexchange.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bhavneshsharma.currencyconvertoropenexchange.ui.screens.home.HomeScreen

@Composable
fun BaseNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavigationRouteItems.HomeScreen.route
    ) {
        composable(
            route = NavigationRouteItems.HomeScreen.route,
            enterTransition = { slideInHorizontally() },
            exitTransition = { slideOutHorizontally() },
            popEnterTransition = { slideInHorizontally() },
            popExitTransition = { slideOutHorizontally() }
        ) {
            HomeScreen()
        }
    }
}