package com.firefly.bikerr_compose.model

data class MyRides (var id : String,var members : List<String>?, val rideDesc: String, val rideStartFrom : String, val rideEndTo: String,val rideDate: String,val rideTitle : String ,val meetupPoint: String, val createdBy : String){
    constructor() : this("",null,"","","","","","","")
}