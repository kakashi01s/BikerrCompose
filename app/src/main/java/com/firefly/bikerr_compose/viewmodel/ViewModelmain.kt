package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.model.Categories
import com.firefly.bikerr_compose.model.Stores
import com.firefly.bikerr_compose.screens.shop.CategoryBanner
import com.firefly.bikerr_compose.screens.shop.SparePartsItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.delay

class ViewModelmain : ViewModel() {

    val loading = mutableStateOf(false)
     var sparePartlist = mutableStateOf(listOf<Stores>())
     var categorylist = mutableStateOf(listOf<Categories>())
    var databaseRef: DatabaseReference? = null


    @Composable
    fun getSpareParts() {
        JetFirestore(path = {
            collection("SpareParts")
        },
            onRealtimeCollectionFetch = { values, exception ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()

                //When documents are fetched based on limit
                sparePartlist.value =  values.getListOfObjects()
            }) {

        }
    }

    @Composable
     fun getCategoryItems() {
        loading.value = true
        JetFirestore(path = {
            collection("Categories")
        },
            onRealtimeCollectionFetch = { values, exception ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()
                //When documents are fetched based on limit
                categorylist.value = values.getListOfObjects()
            }) {
                loading.value = false
        }
    }

    @Composable
    fun CreateStreamUser(){
        val uname  = remember {
            mutableStateOf("")
        }
        val unumber = remember {
            mutableStateOf("")
        }
        val uemail = remember {
            mutableStateOf("")
        }
        val uimg = remember {
            mutableStateOf("")
        }
    val client = ChatClient.instance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    databaseRef!!.child(uid).get().addOnSuccessListener { it ->
        if (it.exists()) {
            uname.value = it.child("userName").value.toString()
            uemail.value = it.child("userEmail").value.toString()
            unumber.value = it.child("userPhone").value.toString()
            uimg.value = it.child("userImage").value.toString()
            Log.d("uemail", uemail.value)

            val user = User(
                id = uid,
                extraData = mutableMapOf(
                    "name" to uname.value,
                    "image" to uimg.value,
                ),
            )
            client.connectUser(
                user = user,
                token = client.devToken(uid)
            ).enqueue {
                if (it.isSuccess) {
                    Log.d("stream", it.data().connectionId)


                } else {
                    Log.d("stream", it.error().message.toString())

                }

            }

        }
    }
}

}