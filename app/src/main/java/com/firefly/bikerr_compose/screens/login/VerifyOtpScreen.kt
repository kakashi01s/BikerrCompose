package com.firefly.bikerr_compose.screens.login

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.MainApplication
import com.firefly.bikerr_compose.activities.LoginActivity
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.apiinterface.WebService
import com.firefly.bikerr_compose.apiinterface.WebServiceCallback
import com.firefly.bikerr_compose.model.User
import com.firefly.bikerr_compose.viewmodel.ViewModellogin
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
fun VerifyOtpScreen(
    loginActivity: LoginActivity,
    viewmodel: ViewModellogin,
    username: String,
    email: String,
    phone: String
)
{

    val focusManager = LocalFocusManager.current
    val otp1 = remember { TextFieldState() }
    val otp2 = remember { TextFieldState() }
    val otp3 = remember { TextFieldState() }
    val otp4 = remember { TextFieldState() }
    val otp5 = remember { TextFieldState() }
    val otp6 = remember { TextFieldState() }
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        HeaderTextOTP()
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly)
        {
            OTPTextField1(otp1,focusManager)
            OTPTextField1(otp2,focusManager)
            OTPTextField1(otp3,focusManager)
            OTPTextField1(otp4,focusManager)
            OTPTextField1(otp5,focusManager)
            OTPTextField1(otp6,focusManager)
        }
        
        ButtonVerify_OTP(
            onClick = {
                           // initService(loginActivity,email,phone)
                        val otp = otp1.text + otp2.text + otp3.text + otp4.text + otp5.text + otp6.text
                        viewmodel.verifyAuthentication(otpText = otp, loginActivity, username, email, phone)


            })
        }
    }



//private fun initService(loginActivity: LoginActivity, email: String, phone: String) {
//    val url = "http://13.232.143.186:8082/api/server"
//    var client: OkHttpClient? = null
//    var retrofit: Retrofit? = null
//    var user: User? = null
//    val callbacks: MutableList<MainApplication.GetServiceCallback> = LinkedList()
//    val cookieManager = CookieManager()
//    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//    client = OkHttpClient.Builder()
//        .readTimeout(0, TimeUnit.MILLISECONDS)
//        .cookieJar(JavaNetCookieJar(cookieManager)).build()
//    try {
//        retrofit = Retrofit.Builder()
//            .client(client)
//            .baseUrl(url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    } catch (e: IllegalArgumentException) {
//        Toast.makeText(loginActivity, e.message, Toast.LENGTH_LONG).show()
//        for (callback in callbacks) {
//            callback.onFailure()
//        }
//        callbacks.clear()
//    }
//    val service: WebService = retrofit!!.create(WebService::class.java)
//    service.addSession(email, phone).enqueue(object : WebServiceCallback<User?>(loginActivity) {
//        override fun onSuccess(response: Response<User?>?) {
//            user = response!!.body()
//            for (callback in callbacks) {
//                callback.onServiceReady(client, retrofit, service)
//            }
//            callbacks.clear()
//            Log.d("tttt", user!!.name.toString())
//        }
//
//        override fun onFailure(call: Call<User?>, t: Throwable) {
//            var handled = false
//            for (callback in callbacks) {
//                handled = callback.onFailure()
//            }
//            callbacks.clear()
//            if (!handled) {
//                super.onFailure(call, t)
//            }
//        }
//
//
//    })
//}


@Composable
private fun HeaderTextOTP() {
    Text(
        text = "Verify",
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color.LightGray
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(text = "OTP,", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.Red)
}


@Composable
private fun OTPTextField1(
    otp: TextFieldState = remember { TextFieldState() },
    focusManager: FocusManager
) {
    val maxChar = 1
    OutlinedTextField(
        value = otp.text,
        onValueChange = {
            otp.text = it.take(maxChar)
            if (it.length == maxChar){
                focusManager.moveFocus(FocusDirection.Right) // Or receive a lambda function
            }
                        },
        label = { Text(text = "") },
        modifier = Modifier.width(40.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onAny = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Right
            )
        }),
        maxLines = 1,
    )
}


@Composable
private fun ButtonVerify_OTP(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp)
    ) {
        Text("VERIFY OTP")
    }
}

