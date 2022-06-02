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
import com.firefly.bikerr_compose.screens.me.profile.ProfileScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelMyProfile
import com.google.firebase.auth.FirebaseAuth

class MyProfileActivity : ComponentActivity() {
    private val viewModel by viewModels<ViewModelMyProfile>()
    val uid = FirebaseAuth.getInstance().uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                        viewModel.getUserDetails(uid =uid!!)

                   ProfileScreen( viewModel,this)
                }
            }
        }
    }
}

