package com.firefly.bikerr_compose.model

import com.firefly.bikerr_compose.data.remote.placesDto.Result

data class Place(
    val nextPageToken: String,
    val results: List<Result>,
)
