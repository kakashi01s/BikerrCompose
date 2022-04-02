package com.firefly.bikerr_compose.apiinterface

import com.firefly.bikerr_compose.model.Order
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RazorpayApiInterface {
    @POST("/getOrderId")
    fun getOrderId(@Body map : HashMap<String, String>): Call<Order>
    @POST("/updateTransaction")
    fun updateTransaction(@Body map : HashMap<String , String>) : Call<String>
    @POST("/sendmail")
    fun sendEmail(@Body map : HashMap<String , String>) : Call<String>
}