package com.firefly.bikerr_compose.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.firefly.bikerr_compose.R
import com.firefly.bikerr_compose.activities.ui.theme.Bikerr_composeTheme
import com.firefly.bikerr_compose.navigation.SetUpLoginGraph
import com.firefly.bikerr_compose.viewmodel.ViewModellogin
import com.google.firebase.auth.FirebaseAuth
import com.skydoves.landscapist.glide.GlideImage

class LoginActivity : ComponentActivity() {
    private lateinit var navController : NavHostController

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Bikerr_composeTheme {
                val viewModel : ViewModellogin = ViewModelProvider(this)[ViewModellogin::class.java]
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {

                }) {

                    navController = rememberNavController()
                    SetUpLoginGraph(navController = navController, this, viewModel)

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
@Composable
fun HeaderText() {
    Column() {
        Text(
            text = "Welcome,",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Sign up to create an account", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.Red)
    }

}

