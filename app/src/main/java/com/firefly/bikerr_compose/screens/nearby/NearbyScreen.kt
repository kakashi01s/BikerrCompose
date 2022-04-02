package com.firefly.bikerr_compose.screens.nearby

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.data.remote.placesDto.Result
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NearbyScreen(
    mainActivityCompose: MainActivityCompose,
)
{
    val viewModelNearby = ViewModelNearby()
    //calling places
    viewModelNearby.getActualLocation(mainActivityCompose)
    var placesList : List<Result> by remember { mutableStateOf(listOf())}
    val placeChip = listOf("FUEL", "GARAGE","ATM", "FOOD","MEDICINE")
    var plcc = placesList
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),

            ) {
            Text(text = "Nearby", fontWeight = FontWeight.Bold, fontSize = 30.sp)

        }
    }) {
//        Column() {
//            Row() {
//                LazyRow(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp),
//                 horizontalArrangement = Arrangement.SpaceBetween){
//                    items(placeChip){ item ->
//                        Box(modifier = Modifier
//                            .clip(shape = RoundedCornerShape(20.dp))
//                            .background(color = Color.LightGray)
//                            .clickable {
//                                if (item == "FOOD" || item.isEmpty()) placesList =
//                                    placesResponse.value
//                                else if (item == "GARAGE") placesList = placesResponseGarage.value
//                                else if (item == "ATM") placesList = placesResponseAtm.value
//                                else if (item == "FUEL") placesList = placesResponseFuel.value
//                                else if (item == "MEDICINE") placesList =
//                                    placesResponseMedicine.value
//                            }
//                            ,
//                            contentAlignment = Alignment.Center) {
//                            Text(modifier = Modifier
//                                .padding(10.dp),text = item, fontSize = 14.sp)
//                        }
//
//                    }
//                }
//            }
//            if (plcc.isEmpty()){
//                placesResponseFuel.value.let {
//                    LazyColumn(){
//                        items(it){
//                            PlacesListItem(it = it, mainActivityCompose = mainActivityCompose)
//                            Log.d("jjjj",it.name)
//                        }
//                    }
//                }
//            }
//            else
//            {
//                plcc.let {
//                    LazyColumn(){
//                        items(it){
//                            PlacesListItem(it = it, mainActivityCompose)
//                            Log.d("jjjj",it.name)
//                        }
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(60.dp))
//        }
//
//        Log.d("hhhh", placesResponse.value.toString())
    }
}




@Composable
fun PlacesListItem(
    it: Result,
    mainActivityCompose: MainActivityCompose
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = it.name, fontWeight = FontWeight.Bold)
            Text(text = "${it.rating}⭐️")
            Text(text = it.vicinity)
            Row( horizontalArrangement = Arrangement.End , modifier = Modifier.fillMaxWidth()) {
                FloatingActionButton(backgroundColor = Color.White,contentColor = Color.Black,
                    onClick = {

                    },
                ) {
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${it.geometry.location.lat},${it.geometry.location.lng}&mode=l"))
                        intent.setPackage("com.google.android.apps.maps")
                        mainActivityCompose.startActivity(intent)
                    }) {
                        Icon(imageVector = Icons.Default.NearMe, contentDescription ="" )
                    }
                }
            }
        }
    }
}







