package com.firefly.bikerr_compose.screens.me.listing

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.firefly.bikerr_compose.activities.CreateListingActivity
import com.firefly.bikerr_compose.activities.MyListingsActivity
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.rent.ImageSlider
import com.firefly.bikerr_compose.viewmodel.ViewModelMyListings
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MyListingsScreen(
    activity: MyListingsActivity,
    viewModel: ViewModelMyListings
) {


    Scaffold(
        topBar = {
            Row( horizontalArrangement = Arrangement.Start) {
                Text(text = "My Listings", fontWeight = FontWeight.Bold, fontSize = 30.sp)
            }
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


        viewModel.vehicleList.value.let { 
            LazyColumn(){
                items(it){ vehicle ->

                        MyListingItem(vehicle = vehicle)

                    
                }
            }
        }
    }
}




@Composable
fun MyListingItem(vehicle: Vehicle) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
        backgroundColor = Color.White,
        elevation = 10.dp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                Row() {
                    Column(horizontalAlignment = Alignment.Start) {
                        ImageSlider(vehicle)
                    }

                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(5.dp)) {
                        Text(text = vehicle.Brand.uppercase(), fontSize = 10.sp, color = Color.DarkGray)
                        Text(text = vehicle.name, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    }
                }
                Row() {
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp)
                                .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                                .clip(
                                    RoundedCornerShape(5.dp)
                                )
                                .background(color = Color(0xFF1cca30)), contentAlignment = Alignment.Center) {
                            Text(text = "₹"+vehicle.price, color = Color.White)
                        }
                    }


                }


            }
        }
    }
}
