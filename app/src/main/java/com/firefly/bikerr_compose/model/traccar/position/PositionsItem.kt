package com.firefly.bikerr_compose.model.traccar.position


data class PositionsItem(
    val id: Int,
    val deviceId: Int,
    val protocol: String,
    val deviceTime: String,
    val fixTime: String,
    val serverTime: String,
    val outdated: Boolean,
    val valid: Boolean,
    val latitude: Float,
    val longitude: Float,
    val altitude: Float,
    val speed: Float,
    val course: Float,
    val address: String,
    val accuracy: Float,
    val network: Network,
    val attributes: Attributes
)