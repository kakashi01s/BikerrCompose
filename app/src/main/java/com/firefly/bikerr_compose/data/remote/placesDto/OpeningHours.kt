package com.firefly.bikerr_compose.data.remote.placesDto


import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean
)