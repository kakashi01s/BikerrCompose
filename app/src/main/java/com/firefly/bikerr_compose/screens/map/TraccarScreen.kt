package com.firefly.bikerr_compose.screens.map

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.firefly.bikerr_compose.MainApplication
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.apiinterface.WebService
import com.firefly.bikerr_compose.apiinterface.WebServiceCallback
import com.firefly.bikerr_compose.model.traccar.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun TraccarScreen(mainActivityCompose: MainActivityCompose) {
 val uid = FirebaseAuth.getInstance().uid
    val email = remember {
        mutableStateOf("")
    }
    val phone = remember {
        mutableStateOf("")
    }

    val databaseRef: DatabaseReference?
    databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    databaseRef.child(uid!!).get().addOnSuccessListener {
        if (it.exists())
        {
            email.value = it.child("userEmail").value as String
            phone.value = it.child("userPhone").value as String

        }
        else
        {
            Log.d("feed", "Failed to get uname")
        }
    }
    CreateTraccarUser(mainActivityCompose,email.value,phone.value)
}

@Composable
fun CreateTraccarUser(mainActivityCompose: MainActivityCompose, email: String, pass: String) {

    val url = "http://13.232.143.186:8082"
    var client: OkHttpClient? = null
    var retrofit: Retrofit? = null
    var user: com.firefly.bikerr_compose.model.User? = null
    val callbacks: MutableList<MainApplication.GetServiceCallback> = LinkedList()
    val cookieManager = CookieManager()
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .cookieJar(JavaNetCookieJar(cookieManager)).build()
    try {
        val gson = GsonBuilder().setLenient()
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()
    } catch (e: IllegalArgumentException) {
        Toast.makeText(mainActivityCompose, e.message, Toast.LENGTH_LONG).show()
        Log.d("tttt", e.message.toString())
    }
    val service: WebService = retrofit!!.create(WebService::class.java)
    service.addSession(email, pass).enqueue(object : WebServiceCallback<com.firefly.bikerr_compose.model.User?>(mainActivityCompose) {
        override fun onSuccess(response: Response<com.firefly.bikerr_compose.model.User?>?) {
            mainActivityCompose.service = service
            user = response!!.body()
            for (callback in callbacks) {
                callback.onServiceReady(client, retrofit, service)
            }
            Log.d("tttt",user.toString())
            callbacks.clear()
        }

        override fun onFailure(call: Call<com.firefly.bikerr_compose.model.User?>, t: Throwable) {
            var handled = false
            for (callback in callbacks) {
                handled = callback.onFailure()
            }
            callbacks.clear()
            if (!handled) {
                super.onFailure(call, t)
            }
        }


    })
}

