package com.firefly.bikerr_compose.model.traccar.device


data class TraccarDevice(
    val id: Int,
    val name: String,
    val uniqueId: String,
    val status: String,
    val disabled: Boolean,
    val lastUpdate: String,
    val positionId: Int,
    val groupId: Int,
    val phone: String,
    val model: String,
    val contact: String,
    val category: String,
    val geofenceIds: List<Int>,
    val attributes: Attributes
)