package com.firefly.bikerr_compose.screens.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.MyDevice
import com.firefly.bikerr_compose.activities.TraccarActivity
import com.firefly.bikerr_compose.activities.TraccarHistoryActivity
import com.firefly.bikerr_compose.viewmodel.ViewModelTraccar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TraccarScreen(mainActivityCompose: TraccarActivity, traccarViewModel: ViewModelTraccar) {
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

    GoogleMaps(mainActivityCompose,traccarViewModel)
   // session()
}

@Composable
fun GoogleMaps(mainActivityCompose: TraccarActivity, traccarViewModel: ViewModelTraccar) {
    val mapView = rememberMapViewWithLifecycle()


    Scaffold(topBar =
    {
    Row(Modifier.background(color = Color.White).padding(10.dp)) {
        Row() {
            Text(text = "Tracker")
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Card(shape = RoundedCornerShape(20.dp), elevation = 5.dp, backgroundColor = Color.LightGray) {
                IconButton(onClick = {
                    val intent  = Intent(mainActivityCompose,TraccarHistoryActivity::class.java)
                    mainActivityCompose.startActivity(intent)
                    mainActivityCompose.finish()
                }) {
                    Icon(imageVector = Icons.Default.History, contentDescription = "", tint = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.width(30.dp))

            Card(shape = RoundedCornerShape(20.dp), elevation = 5.dp, backgroundColor = Color.LightGray) {
                IconButton( onClick = {
                    val intent = Intent(mainActivityCompose,MyDevice::class.java)
                    mainActivityCompose.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription ="",tint = Color.DarkGray)
                }

            }
        }
    }
    },
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
    ,
    isFloatingActionButtonDocked = true) {
        AndroidView({ mapView}) {mapView->
            CoroutineScope(Dispatchers.Main).launch {
                        traccarViewModel.pickup.observe(mainActivityCompose) {
                            val map = mapView.getMapAsync { map ->
                                map.isBuildingsEnabled = true
                                map.uiSettings.isMyLocationButtonEnabled = true

                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 18f))
                                val markerOptions = MarkerOptions()
                                    .position(it)
                                    .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                                    .title("MyBike")
                                map.addMarker(markerOptions)
                                Log.d("yyyy", it.toString())
                            }
                  }
            }
        }
    }
}




@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

