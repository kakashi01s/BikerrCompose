package com.firefly.bikerr_compose.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.screens.map.TraccarScreen
import com.firefly.bikerr_compose.viewmodel.ViewModelTraccar

class TraccarActivity : ComponentActivity() {
    private val traccarViewModel by viewModels<ViewModelTraccar>()
    private val mainHandler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val delay = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TraccarScreen(mainActivityCompose = this,traccarViewModel)

//                    mainHandler.post(object : Runnable {
//                        override fun run() {
                            traccarViewModel.getDevice()
//                            mainHandler.postDelayed(this, 15000)
//                            Log.d("yyyy","del")
//                        }
//                    })


                }
            }
        }
    }

    override fun onResume() {
        mainHandler.postDelayed(Runnable {
            mainHandler.postDelayed(runnable!!,delay.toLong())
            Log.d("yyyy","haha")
        }.also {  runnable = it  },delay.toLong())
        super.onResume()
    }

}

