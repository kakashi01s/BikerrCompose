package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.model.rental.RentCategory
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects

class ViewModelMyListings: ViewModel()
{
    var vehicleList = mutableStateOf(listOf<Vehicle>())
    @Composable
    fun getRentals() {
        JetFirestore(path = {
            collection("Rental")
        },
            onRealtimeCollectionFetch = { values, _ ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()
                //When documents are fetched based on limit
                vehicleList.value =  values.getListOfObjects()
            }) {

            Log.d("listings",vehicleList.value.toString())
        }
    }
}