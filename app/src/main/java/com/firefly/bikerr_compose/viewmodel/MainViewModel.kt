package com.firefly.bikerr_compose.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.model.*
import com.firefly.bikerr_compose.model.rental.RentCategory
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import com.pixplicity.easyprefs.library.Prefs
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {
    val mainHandler = Handler(Looper.getMainLooper())
    private val loading = mutableStateOf(false)
     var sparePartsList = mutableStateOf(listOf<Stores>())
     var unreadMessages = mutableStateOf(0)
     var rentalList = mutableStateOf(listOf<RentCategory>())
     var categorylist = mutableStateOf(listOf<Categories>())
    var vehicleList = mutableStateOf(listOf<Vehicle>())
    var user : MutableState<Users> = mutableStateOf(Users("","","","",""))
    var bookingList = mutableStateOf(listOf(Booking()))
    var blogList = mutableStateOf(listOf(Blog()))
    var bookedDates = mutableStateOf(listOf(""))
    var currentDates = mutableStateOf(listOf(""))



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
    fun checkBooking(regNumber : String)  {
        JetFirestore(path = {
            collection("Rental").document(regNumber).collection("bookings")
        },
            onRealtimeCollectionFetch = { values, _ ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()

                //When documents are fetched based on limit
                bookingList.value =  values.getListOfObjects()
            }) {
            Log.d("bookingDates",bookingList.value.toString())
        }
    }

    @Composable
    fun getBlog()  {
        JetFirestore(path = {
            collection("Blog")
        },
            onRealtimeCollectionFetch = { values, _ ->
                //When all documents are fetched
                //booksList = values.getListOfObjects()

                //When documents are fetched based on limit
                blogList.value =  values.getListOfObjects()
            }) {
            Log.d("bookingDates",blogList.value.toString())
        }
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



    fun getCurrentDates(dateString1:String?, dateString2:String?) {
        Log.d("bookingDatesc",dateString1.toString())
        Log.d("bookingDatesc",dateString2.toString())
        val dates = ArrayList<String>()
        val input =  SimpleDateFormat("dd-MM-yyyy", Locale.US)
        var date1:Date? = null
        var date2:Date? = null
        try
        {
            date1 = dateString2?.let { input.parse(it) }
            date2 = dateString1?.let { input.parse(it) }
        }
        catch (e:ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
            cal1.time = date1

        val cal2 = Calendar.getInstance()
            cal2.time = date2

        while (!cal1.after(cal2))
        {
            val output = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            dates.add(output.format(cal1.time))
            cal1.add(Calendar.DATE, 1)
        }
        currentDates.value = dates.toList()

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

    fun streamUnreadMessages(){
        if (ChatClient.instance().getCurrentUser() != null)
        {
            mainHandler.post(object : Runnable {
                override fun run() {
                    unreadMessages.value = ChatClient.instance().getCurrentUser()?.totalUnreadCount!!
                    mainHandler.postDelayed(this, 1000)
                }
            })
        }

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
