package com.firefly.bikerr_compose.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavHostController
import com.firefly.bikerr_compose.activities.LoginActivity
import com.firefly.bikerr_compose.activities.MainActivityCompose
import com.firefly.bikerr_compose.apiinterface.TraccarApiInterface
import com.firefly.bikerr_compose.model.Users
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.NonCancellable.children
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ViewModellogin(application: Application): AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    var cred: String? = null
    private lateinit var databaseref: DatabaseReference

    private lateinit var retrofit: Retrofit
    private lateinit var retroInterface: TraccarApiInterface


    //Start Login
    fun loginTask(
        loginActivity: LoginActivity,
        phoneNumber: String,
        navHostcontroller: NavHostController,
        email: String,
        username: String
    ) {
        val auth = FirebaseAuth.getInstance()
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("login", "onVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("login", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.w("login", "Invalid request", e)

                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.w("login", "The SMS quota for the project has been exceeded", e)
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("login", "onCodeSent:$verificationId")
                cred = verificationId
                navHostcontroller.navigate("verify_otp_Screen/$username,$email,$phoneNumber")
                // Save verification ID and resending token so we can use them later
//            storedVerificationId = verificationId
//            resendToken = token
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(loginActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    // Verify OTP
    fun verifyAuthentication(
        otpText: String,
        loginActivity: LoginActivity,
        username: String,
        email: String,
        phone: String
    ) {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(cred.toString(), otpText)
        signInWithPhoneAuthCredential(phoneAuthCredential, loginActivity, username, email, phone)
    }


    // signIn if Successful
    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        loginActivity: LoginActivity,
        username: String,
        email: String,
        phone: String
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(loginActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("login", "signInWithCredential:success")
                    val firebaseUser = task.result.user!!.uid
                    Prefs.putString("userId", firebaseUser)
                    Prefs.putString("userName", username)
                    Prefs.putString("userPhone", phone)
                    Prefs.putString("userEmail", email)
                    Prefs.putString(
                        "userImage",
                        "https://firebasestorage.googleapis.com/v0/b/bikerrapp-9ba1d.appspot.com/o/pfp.png?alt=media&token=1e3b49b6-f607-4411-9d31-223182136f52"
                    )
                    databaseref = FirebaseDatabase.getInstance().getReference("Users")
                    val users = Users(
                        username,
                        email,
                        phone,
                        "https://firebasestorage.googleapis.com/v0/b/bikerrapp-9ba1d.appspot.com/o/pfp.png?alt=media&token=1e3b49b6-f607-4411-9d31-223182136f52",
                        firebaseUser
                    )
                    sendDataToFirebase(users, firebaseUser, databaseref, loginActivity)
                    val intent = Intent(loginActivity, MainActivityCompose::class.java)
                    loginActivity.startActivity(intent)
                    loginActivity.finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("login", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.w("login", "The verification code entered was invalid", task.exception)
                    }
                    // Update UI
                }
            }
    }

    private fun sendDataToFirebase(
        users: Users,
        firebaseUser: String,
        databaseRef: DatabaseReference,
        loginActivity: LoginActivity
    ) {
        val gson = GsonBuilder().setLenient()
        retrofit = Retrofit.Builder()
            .baseUrl("http://13.233.254.137:80")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()
        retroInterface = retrofit.create(TraccarApiInterface::class.java)

        traccarlogin(retroInterface, users)
        databaseRef.child(firebaseUser).setValue(users).addOnSuccessListener {
            Toast.makeText(loginActivity, "User Registered Successfully", Toast.LENGTH_LONG).show()
            Log.d("register", "User Registered Successfully")
        }.addOnFailureListener {
            Toast.makeText(loginActivity, "User Registration Error", Toast.LENGTH_LONG).show()
            Log.d("register", it.message.toString())
        }
    }

    private fun traccarlogin(
        retroInterface: TraccarApiInterface,
        users: Users
    ) {
        val map = HashMap<String, String>()
        map["name"] = users.userName
        map["email"] = users.userEmail
        map["password"] = "1234567890"
        retroInterface.createTraccarUser(map = map).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("pppp", response.message().toString())
                Log.d("pppp", "created traccar user")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("pppp", t.message.toString())
                Log.d("pppp", "traccar user create fail ")
            }

        })
    }
}

