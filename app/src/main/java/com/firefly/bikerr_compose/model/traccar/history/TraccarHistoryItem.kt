package com.firefly.bikerr_compose.model.traccar.history


data class TraccarHistoryItem(
    val deviceId: Int,
    val deviceName: String,
    val maxSpeed: Int,
    val averageSpeed: Int,
    val distance: Int,
    val spentFuel: Int,
    val duration: Int,
    val startTime: String,
    val startAddress: String,
    val startLat: Int,
    val startLon: Int,
    val endTime: String,
    val endAddress: String,
    val endLat: Int,
    val endLon: Int,
    val driverUniqueId: Int,
    val driverName: String
)