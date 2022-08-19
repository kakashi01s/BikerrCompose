package com.firefly.bikerr_compose.model

data class Booking(var bookingId: String, val startDate: String?, val endDate: String?, val buyer: Users)
{
    constructor(): this("",null,null,Users("","","","",""))
}
