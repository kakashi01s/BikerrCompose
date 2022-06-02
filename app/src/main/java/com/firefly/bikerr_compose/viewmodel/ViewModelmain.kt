package com.firefly.bikerr_compose.viewmodel

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.activities.RentalItemActivity
import com.firefly.bikerr_compose.model.Categories
import com.firefly.bikerr_compose.model.Stores
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.model.rental.RentCategory
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.pixplicity.easyprefs.library.Prefs
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User

class ViewModelmain : ViewModel() {
    val mainHandler = Handler(Looper.getMainLooper())
    private val loading = mutableStateOf(false)
     var sparePartsList = mutableStateOf(listOf<Stores>())
     var unreadMessages = mutableStateOf(0)
     var rentalList = mutableStateOf(listOf<RentCategory>())
     var categorylist = mutableStateOf(listOf<Categories>())
    var vehicleList = mutableStateOf(listOf<Vehicle>())
    var user : MutableState<Users> = mutableStateOf(Users("","","","",""))


    private var  client : ChatClient = ChatClient.instance()


    @Composable
    fun getSpareParts() {
        JetFirestore(path = {
            collection("SpareParts")
        },
            onRealtimeCollectionFetch = { values, exception ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()

                //When documents are fetched based on limit
                sparePartsList.value =  values.getListOfObjects()
            }) {

        }
    }


    @Composable
    fun getUserDetails(uid :String)
    {
        val uname  = remember {
            TextFieldState()
        }
        val unumber = remember {
            TextFieldState()
        }
        val uemail = remember {
            TextFieldState()
        }
        val uimg = remember {
            TextFieldState()
        }

        uname.text = Prefs.getString("userName")
        uemail.text = Prefs.getString("userEmail")
        unumber.text = Prefs.getString("userPhone")
        uimg.text = Prefs.getString("userImage")

        user.value = Users(userName = uname.text, userEmail = uemail.text, userId = uid, userImage = uimg.text, userPhone = unumber.text)




    }

    @Composable
    fun getRentalCategory() {
        JetFirestore(path = {
            collection("RentalCat")
        },
            onRealtimeCollectionFetch = { values, _ ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()

                //When documents are fetched based on limit
                rentalList.value =  values.getListOfObjects()
            }) {

        }
    }

    @Composable
     fun getCategoryItems() {
        loading.value = true
        JetFirestore(path = {
            collection("Categories")
        },
            onRealtimeCollectionFetch = { values, _ ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()
                //When documents are fetched based on limit
                categorylist.value = values.getListOfObjects()
            }) {
                loading.value = false
        }
    }

    fun streamunreadMessages(){
        mainHandler.post(object : Runnable {
            override fun run() {
                unreadMessages.value = ChatClient.instance().getCurrentUser()?.totalUnreadCount!!
                mainHandler.postDelayed(this, 1000)
                Log.d("nnnn",unreadMessages.value.toString())
            }
        })
    }

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

//            val list =ArrayList<Vehicle>()
//            Column(modifier = Modifier
//                .fillMaxWidth()
//                .height(650.dp)) {
//                rentalList.let {
//                    for (i in it){
//                        if (i.location == selectedCity.text){
//                            list.add(i)
//                        }
//                    }
//                }
//                LazyColumn(modifier = Modifier.fillMaxHeight().padding(15.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(10.dp),) {
//                    items(list){ vehicle ->
//                        RentalListItem(vehicle){
//                            val intent = Intent(rentalActivity, RentalItemActivity::class.java)
//                            intent.putExtra("header",vehicle.name)
//                            intent.putExtra("reg",vehicle.regNumber)
//                            rentalActivity.startActivity(intent)
//                        }
//                    }
//                }
//            }
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


            uname.value = Prefs.getString("userName")
            uemail.value = Prefs.getString("userEmail")
            unumber.value = Prefs.getString("userPhone")
            uimg.value = Prefs.getString("userImage")

            Log.d("uemail", Prefs.getString("userEmail"))

            val user = User(
                id = Prefs.getString("userId"),
                extraData = mutableMapOf(
                    "name" to uname.value,
                    "image" to uimg.value,
                ),
            )

            client.connectUser(
                user = user,
                token = client.devToken(Prefs.getString("userId"))
            ).enqueue {
                if (it.isSuccess) {
                    Log.d("stream", it.data().connectionId)


                } else {
                    Log.d("stream", it.error().message.toString())

                }

            }

        }
    }
