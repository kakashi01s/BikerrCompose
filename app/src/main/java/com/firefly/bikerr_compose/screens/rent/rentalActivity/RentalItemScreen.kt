package com.firefly.bikerr_compose.screens.rent.rentalActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.RentalBookingActivity
import com.firefly.bikerr_compose.activities.RentalItemActivity
import com.firefly.bikerr_compose.viewmodel.ViewModelRentalItem
import com.skydoves.landscapist.glide.GlideImage
import java.util.*

@Composable
fun RentalItemScreen(
    rentalItemActivity: RentalItemActivity,
    item: Map<String?, Any?>
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

                val intent = Intent(rentalItemActivity,RentalBookingActivity::class.java)
                intent.putExtra("id",item["regNumber"].toString())
                rentalItemActivity.startActivity(intent)
                rentalItemActivity.finish()

        }) {
            Text(text = "Continue With Booking")

        }
    })
    {

        val images = listOf(
            item["image1"].toString(),
            item["image2"].toString(),
            item["image3"].toString())


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
                Text(text = item["name"].toString(), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    text = item["price"].toString() + "/day",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = item["location"].toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = item["bookingFrom"].toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = item["bookingTo"].toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
        Log.d("iiii", item["name"].toString())
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

