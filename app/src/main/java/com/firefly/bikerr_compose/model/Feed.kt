package com.firefly.bikerr_compose.model

data class Feed(val caption : String, val comments :Int, val image : String, val likes : Int, val uid : String, val feedId : String ){
    constructor(): this("",0,"", 0,"", "")
}
