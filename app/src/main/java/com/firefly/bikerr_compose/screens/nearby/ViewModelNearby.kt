package com.firefly.bikerr_compose.screens.nearby

import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.apiinterface.Placeservice
import com.firefly.bikerr_compose.data.remote.placesDto.Places
import com.firefly.bikerr_compose.data.remote.placesDto.Result
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelNearby : ViewModel() {
      val placesResponse : MutableState<List<Result>> = mutableStateOf(listOf())
    private lateinit var fusedLocationClient: FusedLocationProviderClient
     fun getActualLocation(mainActivityCompose: MainActivityCompose) {
         fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivityCompose)

         val task = fusedLocationClient.lastLocation

         if (ActivityCompat
                 .checkSelfPermission(
                     mainActivityCompose,
                     android.Manifest.permission.ACCESS_FINE_LOCATION
                 )
             != PackageManager.PERMISSION_GRANTED && ActivityCompat
                 .checkSelfPermission(
                     mainActivityCompose,
                     android.Manifest.permission.ACCESS_COARSE_LOCATION
                 )
             != PackageManager.PERMISSION_GRANTED
         ) {

             ActivityCompat.requestPermissions(
                 mainActivityCompose,
                 arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                 101
             )
             return
         }

         task.addOnSuccessListener {
             if (it != null) {
                 Log.d("pppp", it.latitude.toString())
                 getPlaces(it.latitude, it.longitude)
             }
         }
     }
     private fun getPlaces(lat: Double, lon: Double) {
        val latlngcurrent ="$lat,$lon"
         val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=restaurant&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url = url)
         viewModelScope.launch {
             nearby.enqueue(object : Callback<Places>{
                 override fun onResponse(call: Call<Places>, response: Response<Places>) {
                     val places = response.body()
                     placesResponse.value = places!!.results
                 }
                 override fun onFailure(call: Call<Places>, t: Throwable) {
                     Log.d("fuel",t.localizedMessage)
                 }
             })
         }
}
}









