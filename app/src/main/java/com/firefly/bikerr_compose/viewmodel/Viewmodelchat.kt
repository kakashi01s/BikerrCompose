package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User


class Viewmodelchat: ViewModel() {

    var uname : String? = null
    var unumber : String? = null
    var uemail : String? = null
    var uimg :String? = null
    var databaseRef: DatabaseReference? = null
    val cl = ChatClient.instance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var  streamid : String


    fun loginstream()
    {
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef!!.child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                uname = it.child("userName").value?.toString()
                uemail = it.child("userEmail").value?.toString()
                unumber = it.child("userPhone").value?.toString()
                uimg = it.child("userImage").value?.toString()

                val user = User(
                    id = uid,
                    extraData = mutableMapOf(
                        "name" to "$uname",
                        "image" to "$uimg",
                    ),
                )
                cl.connectUser(
                    user = user,
                    token = cl.devToken(uid)
                ).enqueue {
                    if (it.isSuccess) {
                        Log.d("stream", it.data().connectionId)


                    } else {
                        Log.d("stream", it.error().message.toString())

                    }

                }

            }
        }

    }



}