package com.firefly.bikerr_compose.screens.me.listing

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import com.firefly.bikerr_compose.activities.CreateListingActivity
import com.firefly.bikerr_compose.activities.MyListingsActivity
import com.firefly.bikerr_compose.viewmodel.ViewModelMyListings


@Composable
fun MyListingsScreen(
    activity: MyListingsActivity,
    viewModel: ViewModelMyListings
) {


    Scaffold(
        topBar = {
            Text(text = "My Listing")
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    val intent = Intent(activity, CreateListingActivity::class.java)
                    activity.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                }
            }
        },
    ) {


    }
}


