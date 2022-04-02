package com.firefly.bikerr_compose.data.remote.placesDto


import com.firefly.bikerr_compose.data.remote.placesDto.Geometry
import com.firefly.bikerr_compose.data.remote.placesDto.OpeningHours
import com.firefly.bikerr_compose.data.remote.placesDto.Photo
import com.firefly.bikerr_compose.data.remote.placesDto.PlusCode
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("business_status")
    val businessStatus: String,
    val geometry: Geometry,
    val icon: String,
    val name: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHours,
    val photos: List<Photo>,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCode,
    @SerializedName("price_level")
    val priceLevel: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    val vicinity: String,
    @SerializedName("permanently_closed")
    val permanentlyClosed: Boolean
)