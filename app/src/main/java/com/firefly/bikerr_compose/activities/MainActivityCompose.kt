package com.firefly.bikerr_compose.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.firefly.bikerr_compose.MainApplication
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.apiinterface.Placeservice
import com.firefly.bikerr_compose.apiinterface.WebService
import com.firefly.bikerr_compose.apiinterface.WebServiceCallback
import com.firefly.bikerr_compose.data.remote.placesDto.Places
import com.firefly.bikerr_compose.data.remote.placesDto.Result
import com.firefly.bikerr_compose.model.User
import com.firefly.bikerr_compose.navigation.BottomBarScreen
import com.firefly.bikerr_compose.navigation.BottomNavGraph
import com.firefly.bikerr_compose.viewmodel.ViewModelmain
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.internal.it
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivityCompose : ComponentActivity() {


     var service: WebService? = null
    private val mainViewModel by viewModels<ViewModelmain>()
    private val url = "http://13.232.143.186:8082/"
    var email : String= ""
    val uid = FirebaseAuth.getInstance().uid

        private lateinit var fusedLocationClient: FusedLocationProviderClient
    val db = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                val navController = rememberNavController()
                val context: Context = LocalContext.current
                mainViewModel.getCategoryItems()
                mainViewModel.getSpareParts()
                mainViewModel.CreateStreamUser()
                val settingResultRequest = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult()
                ) { activityResult ->
                    if (activityResult.resultCode == RESULT_OK)
                        Log.d("appDebug", "Accepted")
                    else {
                        Log.d("appDebug", "Denied")
                    }
                }
                checkLocationSetting(
                    context = context,
                    onDisabled = { intentSenderRequest ->
                        settingResultRequest.launch(intentSenderRequest)
                    },
                    onEnabled = { Log.d("llll", "Enabled h")


                        try {
                            if (ContextCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    101
                                )

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                )
                // A surface container using the 'background' color from the theme
               BottomAppbarWithFab(navController =navController , this,mainViewModel)
            }
        }


    }







}


@Composable
fun BottomAppbarWithFab(
    navController: NavHostController,
    mainActivityCompose: MainActivityCompose,
    mainViewModel: ViewModelmain
) {
    val screens = listOf(
        BottomBarScreen.Feed,
        BottomBarScreen.Shop,
        BottomBarScreen.Traccar,
        BottomBarScreen.Nearby,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(backgroundColor = Color.White, onClick = { /*TODO*/ }) {
                IconButton(onClick = {
                                     val intent = Intent(mainActivityCompose,WebActivity::class.java)
                    intent.putExtra("url","http://13.232.143.186:8082")
                    mainActivityCompose.startActivity(intent)
                                     },
                    content = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsMotorsports,
                                contentDescription = ""
                            )
                            Text(text = "Rent", style = TextStyle(fontSize = 12.sp))
                        }
                    },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.Transparent,
                cutoutShape = RoundedCornerShape(60),
                content = {
                    BottomNavigation(
                        elevation = 10.dp,
                        backgroundColor = Color.Black.copy(alpha = 1f),
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()

                    ) {
                            screens.forEach { screen ->
                                AddItem(
                                    screen = screen,
                                    currentDestination = currentDestination,
                                    navController = navController
                                )
                            }
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            )

        }) {

        BottomNavGraph(navHostController = navController, mainActivityCompose,mainViewModel)

    }
}


fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen : BottomBarScreen,
    currentDestination : NavDestination?,
    navController : NavHostController
) {

    BottomNavigationItem(
        modifier = Modifier
            .padding(10.dp)
            .align(Alignment.CenterVertically),
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(modifier = Modifier.weight(4f), imageVector = screen.icon, contentDescription = "")
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = { navController.navigate(screen.route) },
        selectedContentColor = Color.White,
        unselectedContentColor = Color.Gray,
        interactionSource = remember { MutableInteractionSource() },
        alwaysShowLabel = false
    )

}




