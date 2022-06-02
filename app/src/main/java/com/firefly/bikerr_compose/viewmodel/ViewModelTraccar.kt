package com.firefly.bikerr_compose.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firefly.bikerr_compose.apiinterface.TraccarApiInterface
import com.firefly.bikerr_compose.model.traccar.device.TraccarDevice
import com.firefly.bikerr_compose.model.traccar.position.PositionsItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.okhttp.Credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ViewModelTraccar: ViewModel() {

    private lateinit var retrofit: Retrofit
    private lateinit var retroInterface: TraccarApiInterface
    val mainHandler = Handler(Looper.getMainLooper())

    var deviceid : String by mutableStateOf(value= "")
    val longitude: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }
    val latitude: MutableLiveData<Float> by lazy { MutableLiveData<Float>() }



    fun getDevice() {
        var uEmail: String? = null
        var uPhone: String? = null

                uEmail = Prefs.getString("userEmail")
                uPhone = Prefs.getString("userPhone")

                        val gson = GsonBuilder().setLenient()
                        retrofit = Retrofit.Builder()
                            .baseUrl("http://13.233.254.137:8082")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson.create()))
                            .build()
                        retroInterface = retrofit.create(TraccarApiInterface::class.java)

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                        val basic = Credentials.basic(uEmail, "1234567890")
                        retroInterface.getTraccarDevice(basic).enqueue(object : Callback<List<TraccarDevice>>{
                            override fun onResponse(
                                call: Call<List<TraccarDevice>>,
                                response: Response<List<TraccarDevice>>
                            ) {
                                if (response.isSuccessful){
                                    for (i in response.body()!!){
                                        Log.d("gggg",i.positionId.toString())
                                        deviceid = i.positionId.toString()
                                    }

                                    mainHandler.post(object : Runnable {
                                        override fun run() {
                                            getPosition()
                                            mainHandler.postDelayed(this, 1000)
                                            Log.d("yyyy","del")
                                        }
                                    })
                                }
                            }

                            override fun onFailure(call: Call<List<TraccarDevice>>, t: Throwable) {
                                Log.d("gggg",t.message.toString())
                            }

                        })
                    }
        }

    }


    fun getPosition() {
        viewModelScope.launch {
           CoroutineScope(Dispatchers.IO).launch {
               val auth = FirebaseAuth.getInstance().currentUser!!.uid
               var uEmail: String?
               val databaseRef: DatabaseReference?
               databaseRef = FirebaseDatabase.getInstance().getReference("Users")
               databaseRef.child(auth).get().addOnSuccessListener {
                   if (it.exists())
                   {
                       uEmail = it.child("userEmail").value as String

                       val map = HashMap<String, String>()
                       map["email"] = uEmail.toString()
                       map["password"] = "1234567890"

                       val gson = GsonBuilder().setLenient()
                       retrofit = Retrofit.Builder()
                           .baseUrl("http://13.233.254.137:8082")
                           .addConverterFactory(ScalarsConverterFactory.create())
                           .addConverterFactory(GsonConverterFactory.create(gson.create()))
                           .build()
                       retroInterface = retrofit.create(TraccarApiInterface::class.java)

                       val basic = Credentials.basic(uEmail, "1234567890")
                       retroInterface.getDevicePosition(basic,deviceid).enqueue(object : Callback<List<PositionsItem>>{
                           override fun onResponse(
                               call: Call<List<PositionsItem>>,
                               response: Response<List<PositionsItem>>
                           ) {
                               if (response.isSuccessful ){
                                   for (i in response.body()!!){
                                       Log.d("position", i.longitude.toString())
                                       Log.d("position", i.latitude.toString())
                                       latitude.postValue(i.latitude)
                                       longitude.postValue(i.longitude)
                                       Log.d("ttt",deviceid)
                                   }
                               }
                           }

                           override fun onFailure(call: Call<List<PositionsItem>>, t: Throwable) {
                               Log.d("position",deviceid)

                               Log.d("position", t.cause.toString())
                               Log.d("position",t.message.toString())
                           }

                       })
                   }

                   else
                   {
                       Log.d("getd", "Failed to get uname")
                   }
               }
           }
        }
    }



}