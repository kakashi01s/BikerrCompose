package com.firefly.bikerr_compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector?
){
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
        route = "rent",
        title = "Rent",
        icon = Icons.Default.SportsMotorsports,
    )
    object Profile : BottomBarScreen(
        route = "me",
        title = "Me",
        icon = Icons.Default.Person,
    )
    object Null : BottomBarScreen(
        route = "",
        title = "",
        icon = null
    )
}