package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firefly.bikerr_compose.activities.TraccarHistoryActivity
import com.firefly.bikerr_compose.apiinterface.TraccarApiInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.okhttp.Credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ViewModelTraccarHistory: ViewModel() {
    private lateinit var retrofit: Retrofit
    private lateinit var retroInterface: TraccarApiInterface
    private var deviceid: String by mutableStateOf(value = "")

    fun getHistory() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                CoroutineScope(Dispatchers.IO).launch {
                    val auth = FirebaseAuth.getInstance().currentUser!!.uid
                    var uEmail: String?
                    val databaseRef: DatabaseReference?
                    databaseRef = FirebaseDatabase.getInstance().getReference("Users")
                    databaseRef.child(auth).get().addOnSuccessListener {
                        if (it.exists()) {
                            uEmail = it.child("userEmail").value as String

                            val map = HashMap<String, String>()
                            map["email"] = uEmail.toString()
                            map["password"] = "1234567890"

                            val gson = GsonBuilder().setLenient()

                            val moshi = Moshi.Builder() // adapter
                                .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                                .build()
                            retrofit = Retrofit.Builder()
                                .baseUrl("http://13.233.254.137:8082")
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(MoshiConverterFactory.create(moshi))
                                .build()
                            retroInterface = retrofit.create(TraccarApiInterface::class.java)

                            val basic = Credentials.basic(uEmail, "1234567890")
                            retroInterface.getDeviceHistory(
                                basic,
                                deviceid,
                                "2022-05-13T06:00:00Z",
                                "2022-06-03T23:59:00Z"
                            )
                                .enqueue(object : Callback<List<TraccarHistoryActivity>> {
                                    override fun onResponse(
                                        call: Call<List<TraccarHistoryActivity>>,
                                        response: Response<List<TraccarHistoryActivity>>
                                    ) {
                                        Log.d("hhhh", response.body().toString())
                                    }

                                    override fun onFailure(
                                        call: Call<List<TraccarHistoryActivity>>,
                                        t: Throwable
                                    ) {
                                        Log.d("hhhh", t.localizedMessage.toString())
                                    }
                                })


                        } else {
                            Log.d("getd", "Failed to get uname")
                        }
                    }
                }
            }
        }
    }
}