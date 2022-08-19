package com.firefly.bikerr_compose.navigation

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.firefly.bikerr_compose.activities.LoginActivity
import com.firefly.bikerr_compose.screens.login.PhoneNumberScreen
import com.firefly.bikerr_compose.screens.login.RegisterScreen
import com.firefly.bikerr_compose.screens.login.VerifyOtpScreen
import com.firefly.bikerr_compose.viewmodel.ViewModellogin

@ExperimentalMaterialApi
@Composable
fun SetUpLoginGraph(
    navController: NavHostController,
    loginActivity: LoginActivity,
    viewModel: ViewModellogin,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Register.route
    ){

        //Register route
        composable(route = Screens.Register.route)
        {
            RegisterScreen(navController,loginActivity,viewModel)
        }

        //Verify otp route
        composable(
            route = Screens.VerifyOTP.route,
            arguments = listOf(navArgument("name")
                        {
                            type = NavType.StringType
                        },
                        navArgument("email")
                        {
                            type = NavType.StringType
                        },navArgument("phone")
                        {
                            type = NavType.StringType
                        })
        )
        {
            val username = it.arguments?.getString("name").toString()
            val email = it.arguments?.getString("email").toString()
            val phone = it.arguments?.getString("phone").toString()
            Log.d("login",username+email+phone)
           VerifyOtpScreen(loginActivity,viewModel,username,email,phone)
        }

    }
}