package com.firefly.bikerr_compose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.rent.rentalActivity.RentalListScreen

class RentalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Bikerr_composeTheme {
            val name = intent.getStringExtra("name")
                RentalListScreen(this,name
                )
            }
        }
    }
}

