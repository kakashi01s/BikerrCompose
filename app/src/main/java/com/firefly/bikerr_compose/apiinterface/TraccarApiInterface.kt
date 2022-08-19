package com.firefly.bikerr_compose.apiinterface

import com.firefly.bikerr_compose.activities.TraccarHistoryActivity
import com.firefly.bikerr_compose.model.traccar.device.TraccarDevice
import com.firefly.bikerr_compose.model.traccar.position.PositionsItem
import retrofit2.Call
import retrofit2.http.*

interface TraccarApiInterface {
    @POST("/createUser")
    fun createTraccarUser(@Body map : HashMap<String, String>): Call<String>

    @POST("/api/devices")
    fun addDevice(@Header("Authorization") h1:String, @Query("name") name : String, @Query("uniqueId") uniqueId : String): Call<List<TraccarDevice>>

    @GET("/api/devices")
    fun getTraccarDevice(@Header("Authorization") h1:String): Call<List<TraccarDevice>>

    @GET("/api/positions")
    fun getDevicePosition(@Header("Authorization") h1:String, @Query("id") id : String): Call<List<PositionsItem>>

    @GET("/api/reports/trips")
    fun getDeviceHistory(@Header("Authorization") h1:String, @Query("id") id : String,@Query("from") from : String,@Query("to") to: String): Call<List<TraccarHistoryActivity>>


}