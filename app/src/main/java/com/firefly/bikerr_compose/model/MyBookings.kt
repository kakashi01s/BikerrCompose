package com.firefly.bikerr_compose.model

import com.firefly.bikerr_compose.model.rental.Vehicle

data class MyBookings(var bookingId: String, val startDate: String?, val endDate: String?, val buyer: Users,val vehicle: Vehicle?)
{
    constructor(): this("",null,null,Users("","","","",""),null)
}

