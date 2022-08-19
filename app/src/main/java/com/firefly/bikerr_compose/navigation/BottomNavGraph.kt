package com.firefly.bikerr_compose.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.screens.home.FeedScreen
import com.firefly.bikerr_compose.screens.me.MeScreen
import com.firefly.bikerr_compose.screens.rent.RentScreen
import com.firefly.bikerr_compose.screens.shop.ShopScreen
import com.firefly.bikerr_compose.viewmodel.MainViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    mainActivityCompose: MainActivityCompose,
    mainViewModel: MainViewModel
) {
NavHost(
    navController = navHostController,
    startDestination = BottomBarScreen.Feed.route,
){
        composable(route = BottomBarScreen.Feed.route)
        {
            FeedScreen(mainActivityCompose,mainViewModel)
        }
        composable(route = BottomBarScreen.Shop.route)
        {
            ShopScreen(mainActivityCompose,mainViewModel)
        }
        composable(route = BottomBarScreen.Traccar.route)
        {
            RentScreen(mainActivityCompose = mainActivityCompose,mainViewModel)
        }
        composable(route = BottomBarScreen.Profile.route)
        {
           MeScreen(mainActivityCompose = mainActivityCompose,mainViewModel)
        }
}
}