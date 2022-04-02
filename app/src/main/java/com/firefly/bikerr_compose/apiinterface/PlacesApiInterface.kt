package com.firefly.bikerr_compose.apiinterface


import com.firefly.bikerr_compose.data.remote.placesDto.Places
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


const val places_base_url = "https://maps.googleapis.com/"
interface PlacesApiInterface {
    @GET
    fun getNearbyPlaces(@Url url: String):Call<Places>
}
object Placeservice {
    val Placesinstance : PlacesApiInterface
    init{
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(places_base_url)
            .build()

        Placesinstance =  retrofit.create(PlacesApiInterface::class.java)

    }
}