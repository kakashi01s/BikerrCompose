package com.firefly.bikerr_compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.firefly.bikerr_compose.activities.MyBookingsActivity
import com.firefly.bikerr_compose.screens.me.myBookings.MyBookingScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelMyBookings

@Composable
fun MyBookingsNavGraph(
    navHostController: NavHostController,
    mainActivityCompose: MyBookingsActivity,
    mainViewModel: ViewModelMyBookings
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.MyBookingScreen.route,
    ){
        composable(route = Screens.MyBookingScreen.route)
        {
           MyBookingScreen(activity = mainActivityCompose, viewModel = mainViewModel)
        }
        composable(route = Screens.MyBookingItem.route)
        {

        }

    }
}