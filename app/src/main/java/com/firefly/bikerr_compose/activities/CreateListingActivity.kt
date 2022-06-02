package com.firefly.bikerr_compose.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.me.listing.CreateListingScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelCreateListing

class CreateListingActivity : AppCompatActivity() {
    private val viewModel by viewModels<ViewModelCreateListing>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme

                    CreateListingScreen(this,viewModel)

            }

        }
    }
}
