package com.firefly.bikerr_compose.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.rent.rentalActivity.RentalItemScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelRentalItem

class RentalItemActivity : ComponentActivity() {
    private val viewModel by viewModels<ViewModelRentalItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val id = intent.getStringExtra("reg")
            val startDate = intent.getStringExtra("startDate")
            val endDate = intent.getStringExtra("endDate")
            Log.d("dates",startDate.toString())
            Log.d("datee",endDate.toString())
            Bikerr_composeTheme {
                viewModel.GetVehicleForBooking(id = id.toString())

                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {

                }) {
                    RentalItemScreen(this,viewModel.itemBooking.value,startDate,endDate)

                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}