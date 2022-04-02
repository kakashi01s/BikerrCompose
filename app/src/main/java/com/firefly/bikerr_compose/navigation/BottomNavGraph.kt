package com.firefly.bikerr_compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.data.remote.placesDto.Result
import com.firefly.bikerr_compose.screens.home.FeedScreen
import com.firefly.bikerr_compose.screens.map.TraccarScreen
import com.firefly.bikerr_compose.screens.nearby.NearbyScreen
import com.firefly.bikerr_compose.screens.shop.ShopScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelmain


@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    mainActivityCompose: MainActivityCompose,
    mainViewModel: ViewModelmain
) {
NavHost(
    navController = navHostController,
    startDestination = BottomBarScreen.Feed.route,
){
        composable(route = BottomBarScreen.Feed.route)
        {
            FeedScreen(mainActivityCompose)
        }
        composable(route = BottomBarScreen.Shop.route)
        {
            ShopScreen(mainActivityCompose,mainViewModel)
        }
        composable(route = BottomBarScreen.Traccar.route)
        {
            TraccarScreen(mainActivityCompose)
        }
        composable(route = BottomBarScreen.Nearby.route)
        {
           NearbyScreen(mainActivityCompose)
        }

}
}