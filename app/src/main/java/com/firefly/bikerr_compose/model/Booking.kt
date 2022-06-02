package com.firefly.bikerr_compose.model

import java.util.*

data class Booking(val bookingId: String ,val startDate: String?,val endDate: String?,val buyer: Users)
{
    constructor(): this("","","",Users("","","","",""))
}
