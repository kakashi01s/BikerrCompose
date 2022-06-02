package com.firefly.bikerr_compose.model

data class Product(
    var Image1: String,
    var Image2: String,
    var Image3: String,
    var Name: String,
    var Price: String
){
    constructor(): this("","","","","")

}