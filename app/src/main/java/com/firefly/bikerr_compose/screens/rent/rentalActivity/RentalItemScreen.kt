package com.firefly.bikerr_compose.screens.rent.rentalActivity

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.CheckoutBookingActivity
import com.firefly.bikerr_compose.activities.RentalItemActivity
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun RentalItemScreen(
    rentalItemActivity: RentalItemActivity,
    value: Vehicle?,
    startDate: String?,
    endDate: String?
) {

    Scaffold(topBar = {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                Text( text = "Book Your Bike", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }

    }, bottomBar = {
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), onClick = {

            if (value != null) {
                val vehicle = Vehicle(value.name, value.Brand,value.regNumber,value.location,value.price,value.description,value.image1,value.image2,value.image3,value.pickupAddress, owner = Users(value.owner.userName,value.owner.userEmail,value.owner.userPhone,value.owner.userImage,value.owner.userId),value.verified,value.insurancePapers,value.RcFrontImage,value.RcBackImage)
                val gson = Gson()
                val intent = Intent(rentalItemActivity, CheckoutBookingActivity::class.java)
                intent.putExtra("vehicle", gson.toJson(vehicle))
                intent.putExtra("startDate",startDate)
                intent.putExtra("endDate",endDate)
                rentalItemActivity.startActivity(intent)
            }


        }) {
            Text(text = "Continue With Booking")

        }
    })
    {

        val images = listOf(
            value?.image1.toString(),
            value?.image2.toString(),
            value?.image3.toString())


Column {
    Box(
        Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Slider(images)
    }

    Text(
        text = "Details",
        Modifier.padding(start = 10.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    )
    Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(Modifier.width(150.dp)) {
                Text(text = "Name", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Price", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Location", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Booked From :", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Booked Till :", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Column {
                value?.let {

                    Text(text = it.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(
                        text = it.price + "/day",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = it.location,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }
        }
    }
}

    }
}


@Composable
fun Slider(images: List<String>) {
    com.firefly.bikerr_compose.common.Pager(
        items = images,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        itemFraction = .75f,
        overshootFraction = .75f,
        initialIndex = 0,
        itemSpacing = 60.dp,
        contentFactory = { item ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(imageModel = item)

            }
        }
    )
}

