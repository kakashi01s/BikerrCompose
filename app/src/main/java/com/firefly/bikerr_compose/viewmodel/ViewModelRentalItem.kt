package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.jet.firestore.JetFirestore

class ViewModelRentalItem : ViewModel() {


    var itemBooking: MutableState<Vehicle?> = mutableStateOf(null)

    @Composable
    fun GetVehicleForBooking(id: String) {
        JetFirestore(path = {
            collection("Rental").document(id)
        },
            onRealtimeDocumentFetch = { values, exception ->
                //  item.value =  values.metadata
                Log.d("value", values?.data.toString())
                if (values != null) {
                    itemBooking.value = values.toObject(Vehicle::class.java)
                }
                Log.d("value", itemBooking.toString())
            }) {

        }
    }
}