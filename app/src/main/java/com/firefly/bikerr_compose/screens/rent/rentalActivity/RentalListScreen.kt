package com.firefly.bikerr_compose.screens.rent.rentalActivity


import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.RentalActivity
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.accompanist.pager.*
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import kotlin.math.absoluteValue
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.firefly.bikerr_compose.activities.RentalItemActivity
import com.firefly.bikerr_compose.screens.rent.RentalListItem
import com.google.accompanist.pager.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@Composable
fun RentalListScreen(rentalActivity: RentalActivity, name: String?) {
    val selectedCity = remember { TextFieldState() }
    var rentalList by remember { mutableStateOf(listOf<Vehicle>()) }
    Scaffold(topBar = {
        Column {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                Text( text = name.toString(), fontWeight = FontWeight.Bold, fontSize = 30.sp)

            }
            Row {
                SelectedCity(mainActivityCompose = rentalActivity,selectedCity)
            }
        }

    })
    {
        JetFirestore(path = {
            collection("Rental")
        },
            onRealtimeCollectionFetch = { values, exception ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()
                //When documents are fetched based on limit
                rentalList =  values.getListOfObjects()
            }) {

            val list =ArrayList<Vehicle>()
            Column(modifier = Modifier
                .fillMaxWidth()
                .height(650.dp)) {
                rentalList.let { 
                    for (i in it){
                        if (i.location == selectedCity.text){
                                list.add(i)
                            }
                        }
                    }
                LazyColumn(modifier = Modifier.fillMaxHeight().padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp),) {
                    items(list){ vehicle ->
                        RentalListItem(vehicle){
                            val intent = Intent(rentalActivity,RentalItemActivity::class.java)
                            intent.putExtra("header",vehicle.name)
                            intent.putExtra("reg",vehicle.regNumber)
                            rentalActivity.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedCity(
    mainActivityCompose: RentalActivity,
    selectedCity: TextFieldState = remember { TextFieldState() }
) {


    val cityList = listOf("Delhi","Udaipur","Rishikesh","Manali")

    //city dropdown
    var expandedquantity by remember { mutableStateOf(false) }


    var textfieldquantity by remember { mutableStateOf(Size.Zero) }


    val iconquantity = if (expandedquantity)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val maxChar = 2
    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedCity.text,
            onValueChange = {  selectedCity.text = it.take(maxChar)
                if (it.length == maxChar){
                    Toast.makeText(mainActivityCompose,"Please Select a City", Toast.LENGTH_SHORT).show()
                }},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldquantity = coordinates.size.toSize()
                },
            label = {Text("City")},
            trailingIcon = {
                Icon(iconquantity,"contentDescription",
                    Modifier.clickable { expandedquantity = ! expandedquantity })
            }
        )
        DropdownMenu(
            expanded = expandedquantity,
            onDismissRequest = { expandedquantity = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldquantity.width.toDp()})
        ) {
            cityList.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedCity.text = label
                    expandedquantity = false
                }) {
                    Text(text = label)

                }
            }
        }
    }
}



