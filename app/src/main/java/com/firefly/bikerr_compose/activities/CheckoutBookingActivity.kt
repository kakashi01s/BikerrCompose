package com.firefly.bikerr_compose.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.apiinterface.RazorpayApiInterface
import com.firefly.bikerr_compose.model.Booking
import com.firefly.bikerr_compose.model.Order
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pixplicity.easyprefs.library.Prefs
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class CheckoutBookingActivity : ComponentActivity(), PaymentResultWithDataListener {
    private lateinit var retrofit: Retrofit
    lateinit var retroInterface: RazorpayApiInterface
    val db = FirebaseDatabase.getInstance()
    val fs = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var item: Vehicle? = null
    private var startDate: String? = null
    private var endDate: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)
        val gson = GsonBuilder().setLenient()
        retrofit = Retrofit.Builder()
            .baseUrl("http://13.233.254.137:80")
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()
        retroInterface = retrofit.create(RazorpayApiInterface::class.java)
        setContent {

            val gson = Gson()
            item  = gson.fromJson(intent.getStringExtra("vehicle"), Vehicle::class.java)
            startDate = intent.getStringExtra("startDate")
            endDate = intent.getStringExtra("endDate")



            val vehicle : Vehicle = item!!
            Log.d("hahahaha", vehicle.price)
            Bikerr_composeTheme {

                Scaffold(topBar = {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),

                        ) {
                        Text(text = "Checkout", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(text = vehicle.price)
                            Button(onClick = {
                                    val amount = vehicle.price

                                    if (amount.isEmpty()) {
                                        return@Button
                                    } else {
                                        getOrderId(amount)
                                    }

                            }) {
                                Text(text = "Pay")
                            }
                        }
                    }
                }) {
                    Column {
                        StrictMode.setThreadPolicy(
                            StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build()
                        )

                        Text(text = item.toString())
                    }

                }
            }
        }
    }

    private fun getOrderId(amount: String) {
        val map = HashMap<String, String>()
        map["amount"] = amount
        retroInterface.getOrderId(map).enqueue(object : Callback<Order> {
            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@CheckoutBookingActivity, t.localizedMessage, Toast.LENGTH_LONG)
                    .show()
                t.localizedMessage?.let { Log.d("rrrrrr", it) }
            }

            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.body() != null)
                    initiatePayment(amount, response.body()!!)
            }
        })
    }

    private fun initiatePayment(amount: String, order: Order) {
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID(order.getKeyId())
        val amountToPay = "${amount}00"
        try {
            val options = JSONObject()
            options.put("name", "Bikerr")
            options.put("description", "Lets Complete Your Payment")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("order_id", order.getOrderId())
            options.put("amount", amountToPay)//pass amount in currency subunits
            co.setKeyID(order.getKeyId())
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val map = HashMap<String, String>()
        map["orderId"] = p0.toString()
        map["payment_Id"] = p1!!.paymentId.toString()
        map["signature"] = p1.signature.toString()
        map["items"] = Gson().toJson(item).toString()

        val bookingItem = Booking(p0.toString(), startDate = startDate,endDate = endDate, buyer = Users(Prefs.getString("userName"),Prefs.getString("userEmail"),Prefs.getString("userPhone"),Prefs.getString("userImage"),Prefs.getString("userId")))
                val customerMap = HashMap<String, String>()
                customerMap["orderId"] = p0.toString()
                customerMap["Owner"] = item!!.owner.toString()
                customerMap["OwnerMail"] = item!!.owner.userEmail

                customerMap["regNumber"] = item!!.regNumber
                customerMap["pickUpAddress"] = item!!.pickupAddress


                retroInterface.updateTransaction(map).enqueue(object : Callback<String> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.body().equals("success")) {
                            Toast.makeText(
                                this@CheckoutBookingActivity,
                                "Payment successful",
                                Toast.LENGTH_LONG
                            ).show()

                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.BASIC_ISO_DATE
                            val formatted = current.format(formatter)

                            fs.collection("Rental").document(item!!.regNumber).collection("bookings").document(p0.toString()).set(bookingItem)

                            db.getReference("RentalBookings").child(Prefs.getString("userId")).child(formatted).child(p0.toString()).setValue(bookingItem)
                            retroInterface.sendEmail(customerMap).enqueue(object :
                                Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    Toast.makeText(this@CheckoutBookingActivity,"Booking Made",Toast.LENGTH_LONG).show()
                                    this@CheckoutBookingActivity.finish()
                                    Log.d("Emailconfirm", "Email confirm")
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("Emailconfirm", "Email fail")
                                }

                            })
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@CheckoutBookingActivity, t.message, Toast.LENGTH_LONG)
                            .show()
                    }
                })

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show()
    }


}
