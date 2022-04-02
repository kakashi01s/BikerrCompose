package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.firefly.bikerr_compose.MainApplication
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.apiinterface.WebService
import com.firefly.bikerr_compose.navigation.SetUpLoginGraph
import com.firefly.bikerr_compose.viewmodel.ViewModellogin
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class LoginActivity : ComponentActivity() {
    lateinit var navController : NavHostController

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bikerr_composeTheme() {
                val viewModel : ViewModellogin = ViewModelProvider(this).get(ViewModellogin::class.java)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navController = rememberNavController()
                    SetUpLoginGraph(navcontroller = navController,this,viewModel, )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this, MainActivityCompose::class.java)
            this.startActivity(intent)
            finish()
    }

}


}
