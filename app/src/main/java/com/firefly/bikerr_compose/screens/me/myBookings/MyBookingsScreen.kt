package com.firefly.bikerr_compose.screens.me.myBookings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Checklist
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.MyBookingsActivity
import com.firefly.bikerr_compose.model.MyBookings
import com.firefly.bikerr_compose.screens.me.TopAppBarDropdownMenu
import com.firefly.bikerr_compose.screens.rent.ImageSlider
import com.firefly.bikerr_compose.screens.rent.rentalActivity.SelectedCity
import com.firefly.bikerr_compose.viewmodel.ViewModelMyBookings
import gen._base._base_java__rjava_resources.srcjar.R.id.text

@Composable
fun MyBookingScreen(activity: MyBookingsActivity, viewModel: ViewModelMyBookings) {


    Scaffold(topBar = {
        Row( horizontalArrangement = Arrangement.Start) {
            Text(text = "My Bookings", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }


    }) {
        val list = viewModel.list
        if (viewModel.loading.value)
        {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
            }

        }
        else
        {
            if (list.value.isNotEmpty())
            {
                LazyColumn(){
                    items(list.value.filter { it.vehicle != null }){ booking ->
                        MyBookingsItem(item = booking) {

                        }
                    }
                }
            }
            else
            {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Row() {
                        Icon(imageVector = Icons.TwoTone.Checklist, contentDescription = "")
                        Text(text = "No Booking's Found")
                    }

                }
            }


        }

        Log.d("mybooking",list.value.toString())
    }




}


@Composable
fun MyBookingsItem(item : MyBookings, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
        backgroundColor = Color.White,
        elevation = 10.dp
    ) {
        Column(modifier = Modifier
            .clickable {
                onClick.invoke()
            }
            .fillMaxWidth()
            .padding(10.dp)) {
            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                Row() {

                    Column(horizontalAlignment = Alignment.Start) {
                        item.vehicle?.let { ImageSlider(it) }
                    }

                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(5.dp)) {
                        item.vehicle?.Brand?.let { Text(text = it.uppercase(), fontSize = 10.sp, color = Color.DarkGray) }
                        item.vehicle?.let { Text(text = it.name, fontWeight = FontWeight.Bold, color = Color.DarkGray) }
                        Text(text = item.startDate.toString(), fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Text(text = item.endDate.toString(), fontWeight = FontWeight.Bold, color = Color.DarkGray)
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
                            item.vehicle?.price.let { Text(text = "₹$it", color = Color.White) }
                        }
                    }


                }


            }
        }
    }
}