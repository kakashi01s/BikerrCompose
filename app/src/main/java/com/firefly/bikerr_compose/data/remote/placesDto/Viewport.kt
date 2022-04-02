package com.firefly.bikerr_compose.data.remote.placesDto


import com.firefly.bikerr_compose.data.remote.placesDto.Northeast
import com.firefly.bikerr_compose.data.remote.placesDto.Southwest

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)