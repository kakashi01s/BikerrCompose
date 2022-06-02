package com.firefly.bikerr_compose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.rent.rentalActivity.RentalBookingScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelRentalBooking

class RentalBookingActivity : ComponentActivity() {
    private val viewModel by viewModels<ViewModelRentalBooking>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val id = intent.getStringExtra("id")
            Bikerr_composeTheme {
                viewModel.GetVehicleForBooking(id = id!!)
                // A surface container using the 'background' color from the theme
                RentalBookingScreen(viewModel,this,viewModel.itemBooking.value)

            }
        }
    }
}

