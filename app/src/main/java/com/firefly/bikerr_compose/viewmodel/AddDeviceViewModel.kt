package com.firefly.bikerr_compose.viewmodel

import android.content.Intent
import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firefly.bikerr_compose.activities.MyDevice
import com.firefly.bikerr_compose.activities.TraccarActivity
import com.firefly.bikerr_compose.apiinterface.TraccarApiInterface
import com.firefly.bikerr_compose.model.traccar.device.TraccarDevice
import com.google.gson.GsonBuilder
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.okhttp.Credentials
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class AddDeviceViewModel: ViewModel() {

    private lateinit var retrofit: Retrofit
    private lateinit var retroInterface: TraccarApiInterface

    fun addDevices(myDevice: MyDevice,
                   deviceName: String,
                   id: String) {
        viewModelScope.launch {
            val uEmail = Prefs.getString("userEmail")
            val gson = GsonBuilder().setLenient()
            retrofit = Retrofit.Builder()
                .baseUrl("http://13.233.254.137:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build()
            retroInterface = retrofit.create(TraccarApiInterface::class.java)

            val basic = Credentials.basic(uEmail, "1234567890")
            retroInterface.addDevice(basic, deviceName, uniqueId = id)
                .enqueue(object : Callback<List<TraccarDevice>> {
                    override fun onResponse(
                        call: Call<List<TraccarDevice>>,
                        response: Response<List<TraccarDevice>>
                    ) {
                        if (response.isSuccessful) {
                            for (i in response.body()!!) {
                                Log.d("addDevices", response.message().toString())
                                val intent = Intent(myDevice, TraccarActivity::class.java)
                                myDevice.startActivity(intent)
                                myDevice.finish()
                            }

                        }
                    }

                    override fun onFailure(call: Call<List<TraccarDevice>>, t: Throwable) {
                        Log.d("addDevices", t.message.toString())
                    }

                })
        }


    }
    }