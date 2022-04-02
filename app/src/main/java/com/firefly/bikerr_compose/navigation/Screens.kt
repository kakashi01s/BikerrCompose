package com.firefly.bikerr_compose.navigation

sealed class Screens(val route : String){
    object Home: Screens(route = "home_Screen")
    object Register : Screens(route = "register_Screen")
    object VerifyOTP : Screens(route = "verify_otp_Screen/{name},{email},{phone}")

}
