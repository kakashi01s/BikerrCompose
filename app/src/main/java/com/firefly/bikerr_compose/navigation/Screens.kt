package com.firefly.bikerr_compose.navigation

sealed class Screens(val route : String){
    object Register : Screens(route = "register_Screen")
    object VerifyOTP : Screens(route = "verify_otp_Screen/{name},{email},{phone}")

    object MyBookingScreen : Screens(route = "my_bookings")
    object MyBookingItem : Screens(route = "my_bookingsItem")
}
