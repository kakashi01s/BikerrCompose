package com.firefly.bikerr_compose.apiinterface

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.assistant.appactions.suggestions.client.ServiceException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class WebServiceCallback<T>(private val context: Context) : Callback<T> {
    abstract fun onSuccess(response: Response<T>?)
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response)
        } else {
            onFailure(call, ServiceException(response.message()))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Toast.makeText(context, "error" + ": " + t.message, Toast.LENGTH_LONG).show()
        Log.d("tttt",t.message.toString())
    }
}