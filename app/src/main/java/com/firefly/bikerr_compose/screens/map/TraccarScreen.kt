package com.firefly.bikerr_compose.screens.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.AddDevice
import com.firefly.bikerr_compose.activities.TraccarActivity
import com.firefly.bikerr_compose.activities.TraccarHistoryActivity
import com.firefly.bikerr_compose.viewmodel.ViewModelTraccar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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
    val pickup: MutableLiveData<LatLng> by lazy { MutableLiveData<LatLng>() }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
    , floatingActionButton = {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {

                FloatingActionButton(onClick = { /*TODO*/ }, backgroundColor = Color.White) {
                    IconButton(onClick = {
                        val intent  = Intent(mainActivityCompose,TraccarHistoryActivity::class.java)
                        mainActivityCompose.startActivity(intent)
                        mainActivityCompose.finish()
                    }) {
                        Icon(imageVector = Icons.Default.History, contentDescription = "", tint = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                FloatingActionButton(onClick = { /*TODO*/ }, backgroundColor = Color.White) {
                    IconButton( onClick = {
                        val intent = Intent(mainActivityCompose,AddDevice::class.java)
                        mainActivityCompose.startActivity(intent)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription ="",tint = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                FloatingActionButton(onClick = { /*TODO*/ }, backgroundColor = Color.White) {
                    IconButton(onClick = {

                        pickup.observe(mainActivityCompose) {
                            val gmmIntentUri =
                                Uri.parse("google.navigation:q=${it.latitude},${it.longitude}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            mainActivityCompose.startActivity(mapIntent)
                            mainActivityCompose.finish()

                        }

                    }) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = "",tint = Color.DarkGray)
                    }
                }
            }

        }, floatingActionButtonPosition = FabPosition.End,
    isFloatingActionButtonDocked = false) {
        AndroidView({ mapView}) {mapView->
            CoroutineScope(Dispatchers.Main).launch {
                var lat: Float
                var lon: Float
                traccarViewModel.latitude.observe(mainActivityCompose) { data ->
                    Log.d("yyyy", data.toString())
                    lat = data
                    traccarViewModel.longitude.observe(mainActivityCompose) { data ->
                        Log.d("yyyy", data.toString())
                        lon = data

                        pickup.postValue(LatLng(lat.toDouble(), lon.toDouble()))
                        pickup.observe(mainActivityCompose) {
                            val map = mapView.getMapAsync { map ->
                                map.isBuildingsEnabled = true
                                map.uiSettings.isMyLocationButtonEnabled = true
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 18f))
                                val markerOptions = MarkerOptions()
                                    .title("MyBike")
                                    .position(it)
                                    .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation))
                                map.addMarker(markerOptions)
                                Log.d("yyyy", it.toString())

                            }
                        }

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

