package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.model.MyRides
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects

class ViewModelMessagesActivity: ViewModel() {
    var planList = mutableStateOf(listOf<MyRides>())
    @Composable
    fun getMyPlans(channelId: String) {
        JetFirestore(path = {
            collection("Rides").document(channelId).collection("Plans")
        },
            onRealtimeCollectionFetch = { values, _ ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()
                //When documents are fetched based on limit
                planList.value =  values.getListOfObjects()
            }) {

            Log.d("ridess",planList.value.toString())
        }
    }

}