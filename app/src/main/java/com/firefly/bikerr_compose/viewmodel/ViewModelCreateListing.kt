package com.firefly.bikerr_compose.viewmodel

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firefly.bikerr_compose.activities.CreateListingActivity
import com.firefly.bikerr_compose.activities.MyListingsActivity
import com.firefly.bikerr_compose.model.Booking
import com.firefly.bikerr_compose.model.Users
import com.firefly.bikerr_compose.model.rental.Vehicle
import com.firefly.bikerr_compose.screens.login.TextFieldState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pixplicity.easyprefs.library.Prefs

class ViewModelCreateListing: ViewModel() {

    private val allPhotosUploaded =  mutableStateOf(false)
    private val openDialog =   mutableStateOf(false)
    private val vehicleImageUrl1 =   TextFieldState()
    private val vehicleImageUrl2 = TextFieldState()
    private val vehicleImageUrl3 = TextFieldState()
    private val vehicleRCFrontUrl = TextFieldState()
    private val vehicleRCBackUrl = TextFieldState()
    private val vehicleInsuranceUrl = TextFieldState()
    val vehicleFullNumber = TextFieldState()

    fun upload1ImageToFirebase(
        fileUri: Uri,
        imageNo: String) {
        val fileName = vehicleFullNumber.text + imageNo + ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("Rental/${vehicleFullNumber.text}/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.d("iiiii", imageUrl)
                    vehicleImageUrl1.text = imageUrl
                }
            }
            .addOnFailureListener { e ->
                print(e.message)
            }
    }
    fun upload2ImageToFirebase(
        fileUri: Uri,
        imageNo: String) {
        val fileName = vehicleFullNumber.text + imageNo + ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("Rental/${vehicleFullNumber.text}/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.d("iiiii", imageUrl)
                    vehicleImageUrl2.text = imageUrl
                }
            }
            .addOnFailureListener { e ->
                print(e.message)
            }
    }
    fun upload3ImageToFirebase(
        fileUri: Uri,
        imageNo: String) {
        val fileName = vehicleFullNumber.text + imageNo + ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("Rental/${vehicleFullNumber.text}/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.d("iiiii", imageUrl)
                    vehicleImageUrl3.text = imageUrl
                }
            }
            .addOnFailureListener { e ->
                print(e.message)
            }
    }
    fun uploadRcFrontImageToFirebase(
        fileUri: Uri,
        imageNo: String) {
        val fileName = vehicleFullNumber.text + imageNo + ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("Rental/${vehicleFullNumber.text}/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.d("iiiii", imageUrl)
                    vehicleRCFrontUrl.text = imageUrl
                }
            }
            .addOnFailureListener { e ->
                print(e.message)
            }
    }
    fun uploadRcBackImageToFirebase(
        fileUri: Uri,
        imageNo: String
    ) {
        val fileName = vehicleFullNumber.text + imageNo + ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("Rental/${vehicleFullNumber.text}/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.d("iiiii", imageUrl)
                    vehicleRCBackUrl.text = imageUrl
                }
            }
            .addOnFailureListener { e ->
                print(e.message)
            }
    }

    fun uploadInsuranceImageToFirebase(
        fileUri: Uri,
        imageNo: String,
        vehicleCity: TextFieldState,
        vehicleDescription: TextFieldState,
        vehicleName: TextFieldState,
        vehiclePickupAddress: TextFieldState,
        vehiclePrice: TextFieldState,
        selectedBrand: TextFieldState,
        activity: CreateListingActivity
    ) {
        val fileName = vehicleFullNumber.text + imageNo + ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("Rental/${vehicleFullNumber.text}/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Log.d("last", imageUrl)
                    allPhotosUploaded.value = true
                    vehicleInsuranceUrl.text = imageUrl
                    sendDataToRentalsFirebase(vehicleCity,vehicleDescription,vehicleName,vehiclePickupAddress,vehiclePrice,activity,selectedBrand)
                }
            }
            .addOnFailureListener { e ->
                Log.d("viiiii",e.message.toString())
            }
    }

     private fun sendDataToRentalsFirebase(
         vehicleCity: TextFieldState,
         vehicleDescription: TextFieldState,
         vehicleName: TextFieldState,
         vehiclePickupAddress: TextFieldState,
         vehiclePrice: TextFieldState,
         activity: CreateListingActivity,
         selectedBrand: TextFieldState
     ) {
        val firestoreRef = FirebaseFirestore.getInstance()

                Log.d("viiiii", vehicleImageUrl1.text)
                val listing = Vehicle(
                    vehicleName.text,
                    selectedBrand.text.uppercase(),
                    vehicleFullNumber.text,
                    vehicleCity.text.uppercase(),
                    vehiclePrice.text,
                    vehicleDescription.text,
                    vehicleImageUrl1.text,
                    vehicleImageUrl2.text,
                    vehicleImageUrl3.text,
                    vehiclePickupAddress.text,
                    Users(
                        Prefs.getString("userName"),
                        Prefs.getString("userEmail"),
                        Prefs.getString("userPhone"),
                        Prefs.getString("userImage"),
                        Prefs.getString("userId")
                    ),
                    verified = false,
                    insurancePapers = vehicleInsuranceUrl.text,
                    vehicleRCFrontUrl.text,
                    vehicleRCBackUrl.text
                )
                firestoreRef.collection("Rental")
                    .document(vehicleFullNumber.text).set(listing)
                    .addOnCompleteListener {
                        Toast.makeText(
                            activity,
                            "Listing Created",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("uploadpost", "Listing Created")
                        openDialog.value = true
                    }
                    .addOnSuccessListener {
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Listing Created")
                        builder.setMessage("Please Wait While we verify Your Listing, Do you Want to Create Another Listing?")
                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            Toast.makeText(activity, android.R.string.yes, Toast.LENGTH_SHORT).show()
                        }
                        builder.setNegativeButton(android.R.string.no) { dialog, which ->
                            activity.finish()
                        }
                        builder.show()
                    }


        }
    }

