package com.firefly.bikerr_compose.data.remote.placesDto


import com.google.gson.annotations.SerializedName

data class Photo(
    val height: Int,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String,
    val width: Int
)