package com.firefly.bikerr_compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route : String,
    val title : String,
    val icon : ImageVector){
    object Feed : BottomBarScreen(
        route = "feed",
        title = "Feed",
        icon = Icons.Default.DynamicFeed,
    )
    object Shop : BottomBarScreen(
        route = "shop",
        title = "Shop",
        icon = Icons.Default.ShoppingCart,
    )
    object Traccar : BottomBarScreen(
        route = "traccar",
        title = "Track",
        icon = Icons.Default.MyLocation,
    )
    object Nearby : BottomBarScreen(
        route = "Nearby",
        title = "Nearby",
        icon = Icons.Default.NearMe,
    )
}