package com.firefly.bikerr_compose.model.rental

import com.firefly.bikerr_compose.model.Booking
import com.firefly.bikerr_compose.model.Users


data class Vehicle(
    var name: String,
    var Brand: String,
    var regNumber: String,
    var location: String,
    var price: String,
    var description: String,
    var image1: String,
    var image2: String,
    var image3: String,
    var pickupAddress: String,
    var owner: Users,
    var verified: Boolean,
    var insurancePapers: String,
    var RcFrontImage: String,
    var RcBackImage: String
)
{
    constructor(): this("","","","","","","","","","",Users("","","","",""),false,"","","")
}
