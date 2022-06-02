package com.firefly.bikerr_compose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.rent.rentalActivity.RentalItemScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelRentalItem

class RentalItemActivity : ComponentActivity() {
    private val viewModel by viewModels<ViewModelRentalItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("reg")
        setContent {
            viewModel.getVehicle(id = id!!)
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                  RentalItemScreen(this,viewModel.item.value)
                }
            }
        }
    }
}
