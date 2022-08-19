package com.firefly.bikerr_compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector?
){
    object Feed : BottomBarScreen(
        route = "feed",
        title = "",
        icon = Icons.Default.DynamicFeed,
    )
    object Shop : BottomBarScreen(
        route = "shop",
        title = "",
        icon = Icons.Default.ShoppingCart,
    )
    object Traccar : BottomBarScreen(
        route = "rent",
        title = "",
        icon = Icons.Default.SportsMotorsports,
    )
    object Profile : BottomBarScreen(
        route = "me",
        title = "",
        icon = Icons.Default.AccountCircle,
    )
    object Null : BottomBarScreen(
        route = "",
        title = "",
        icon = null
    )
}