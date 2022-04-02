package com.firefly.bikerr_compose.data.remote.placesDto


import com.firefly.bikerr_compose.model.Place
import com.google.gson.annotations.SerializedName

data class Places(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val nextPageToken: String,
    val results: List<Result>,
    val status: String
)

fun Places.toPlace() : Place{
    return Place(
        nextPageToken = nextPageToken,
        results = results
    )
}
