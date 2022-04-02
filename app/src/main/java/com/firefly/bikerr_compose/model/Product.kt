package com.firefly.bikerr_compose.model

import com.google.firebase.firestore.ServerTimestamp

data class Product(
    var Image: String ,
    var Name: String ,
    var Price: Int
){
    constructor(): this("","",0)
}