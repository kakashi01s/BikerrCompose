package com.firefly.bikerr_compose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.me.listing.MyListingsScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelMyListings

class MyListingsActivity : ComponentActivity(){
    private val viewModel by viewModels<ViewModelMyListings>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            viewModel.getRentals()
            Bikerr_composeTheme {
                  MyListingsScreen(this,viewModel)

            }
        }
    }
}

