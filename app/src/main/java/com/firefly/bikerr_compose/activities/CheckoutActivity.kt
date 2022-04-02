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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.apiinterface.RazorpayApiInterface
import com.firefly.bikerr_compose.model.CartItem
import com.firefly.bikerr_compose.model.Order
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.android.libraries.places.internal.it
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
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

class CheckoutActivity : ComponentActivity(), PaymentResultWithDataListener {
    lateinit var retrofit: Retrofit
    lateinit var retroInterface: RazorpayApiInterface
    val db = FirebaseDatabase.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val oList : List<CartItem> = listOf()
    private val total =   TextFieldState()
    private val Name =  TextFieldState()
    private val Address =  TextFieldState()
    private val Phone =  TextFieldState()
    private val Pincode =  TextFieldState()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)
        val gson = GsonBuilder().setLenient()
        retrofit = Retrofit.Builder()
            .baseUrl("http://13.232.143.186:80")
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()
        retroInterface = retrofit.create(RazorpayApiInterface::class.java)
        setContent {
            total.text = intent.getLongExtra("total",0).toString()
            Log.d("total", total.text)
            Bikerr_composeTheme {
                val focusManager = LocalFocusManager.current
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
                            Text(text = total.text)
                            Button(onClick = {
                                if (Address.text.isNotEmpty() && Name.text.isNotEmpty() && Pincode.text.isNotEmpty() && Phone.text.isNotEmpty()) {
                                    val amount = total.text

                                    if (amount.isEmpty()) {
                                      return@Button
                                    }
                                    else{
                                        getOrderId(amount)
                                    }

                                }
                                else{
                                    Toast.makeText(this@CheckoutActivity,"Please Fill All Details", Toast.LENGTH_LONG).show()
                                }
                            }) {
                                Text(text = "Pay")
                            }
                        }
                    }
                }) {
                    Column {
                        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
                        GetOrderItems()
                        OrdernameTextField(username = Name, focusManager = focusManager)
                        OrderPhoneTextField(focusManager = focusManager, username = Phone)
                        OrderAddressTextField(focusManager = focusManager, username = Address)
                        PincodeTextField(focusManager = focusManager, username = Pincode)

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
                    Toast.makeText(this@CheckoutActivity, t.localizedMessage, Toast.LENGTH_LONG).show()

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
        val amounttopay = "${amount}00"
        try {
            val options = JSONObject()
            options.put("name","Bikerr")
            options.put("description","Lets Complete Your Payment")
            options.put("theme.color", "#3399cc")
            options.put("currency","INR")
            options.put("order_id", order.getOrderId())
            options.put("amount", amounttopay)//pass amount in currency subunits
            co.setKeyID(order.getKeyId())
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val map = HashMap<String, String>()
        map["orderId"] = p0.toString()
        map["payment_Id"] = p1!!.paymentId.toString()
        map["signature"] = p1.signature.toString()
        map["name"] = Name.text
        map["number"] = Phone.text
        map["address"] = Address.text
        map["pin"] = Pincode.text
        map["items"] = oList.toString()
        db.getReference("Users").child(uid!!).get()
            .addOnSuccessListener {
                val emailId = it.child("email").value.toString()
                val customerMap = HashMap<String, String>()
                customerMap["orderId"] = p0.toString()
                customerMap["name"] = Name.text.toString()
                customerMap["PhoneNumber"] = Phone.text.toString()
                customerMap["Address"] = Address.text.toString()
                customerMap["Items"] = oList.toString()
                customerMap["email"] = emailId

                retroInterface.updateTransaction(map).enqueue(object : Callback<String> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.body().equals("success")) {
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Payment successful",
                                Toast.LENGTH_LONG
                            ).show()

                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.BASIC_ISO_DATE
                            val formatted = current.format(formatter)
                            db.getReference("ConfirmedOrders").child("$formatted/$p0").setValue(customerMap)
                            db.getReference("Cart").child(uid).setValue(null)
                            retroInterface.sendEmail(customerMap).enqueue(object : Callback<String>{
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    Log.d("Emailconfirm", "Email confirm")
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("Emailconfirm", "Email fail")
                                }

                            })
                            val intent = Intent(this@CheckoutActivity, MainActivityCompose::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@CheckoutActivity, t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
    }
    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show()
    }


    @Composable
    fun GetOrderItems() {
        val orderItemList = remember {
            mutableStateListOf<CartItem>()
        }
        db.getReference("Orders").child(uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        for (i in snapshot.children)
                        {
                            val items = i.getValue(CartItem::class.java)
                            orderItemList.add(items!!)
                        }
                        }
                    Log.d("cartitems", orderItemList.toString())
                    }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("cartitems", "loadPost:onCancelled", error.toException())
                }
            })

            orderItemList.let {
                LazyColumn(){
                    items(it){
                        OrderItems(it)
                    }
                }

        }

    }
}

@Composable
private fun OrdernameTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Name") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}
@Composable
private fun OrderAddressTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Address") },
        leadingIcon = { Icon(imageVector = Icons.Filled.LocationCity, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
private fun OrderPhoneTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "Phone") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Phone, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
private fun PincodeTextField(username : TextFieldState = remember { TextFieldState() },focusManager: FocusManager) {
    OutlinedTextField(
        value = username.text,
        onValueChange = { username.text = it },
        label = { Text(text = "PinCode") },
        leadingIcon = { Icon(imageVector = Icons.Filled.Pin, contentDescription = "Usericon") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Down
            )
        }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        maxLines = 1
    )
}

@Composable
fun OrderItems(cartItem: CartItem) {
    Column()
    {
        Row()
            {
                Text(text = "Name : ", fontWeight = FontWeight.Bold)
                Text(text = cartItem.name.toString(), fontWeight = FontWeight.Bold)
            }
            Row()
            {
                Text(text = "Price : ", fontWeight = FontWeight.Bold)
                Text(text = cartItem.price.toString())
            }
    }
}



