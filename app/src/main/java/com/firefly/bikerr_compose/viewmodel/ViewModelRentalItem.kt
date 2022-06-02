package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jet.firestore.JetFirestore

class ViewModelRentalItem : ViewModel(){

    var item = mutableStateOf(mapOf<String?,Any?>())

    @Composable
    fun getVehicle(id : String){
        JetFirestore(path = {
            collection("Rental").document(id)
        },
            onRealtimeDocumentFetch = { values, exception ->
              //  item.value =  values.metadata
                Log.d("value",values?.data.toString())
                item.value = values?.data as Map<String?, Any?>
                Log.d("value",item.toString())
            }) {

        }
    }
}