package com.firefly.bikerr_compose.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.model.MyBookings
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pixplicity.easyprefs.library.Prefs

class ViewModelMyBookings: ViewModel() {
    val list = mutableStateOf(listOf(MyBookings()))
    val loading = mutableStateOf(true)

    fun getMyBookings(){
        val db = FirebaseDatabase.getInstance()
        db.getReference("RentalBookings").child(Prefs.getString("userId")).orderByChild("bookingId")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                    Log.d("bookings", error.message)

                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val children = snapshot.children
                    children.forEach {
                        val data = it.getValue(MyBookings::class.java)
                        if (data != null) {
                            list.value += data
                        }
                    }


                    Log.d("booking", list.value.toString())
                    loading.value = false

                }
            })

    }
}