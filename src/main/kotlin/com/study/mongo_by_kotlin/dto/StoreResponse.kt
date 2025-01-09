package com.study.mongo_by_kotlin.dto

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class StoreResponse(
    val id: String?,
    val name: String,
    val location: GeoJsonPoint,
    val reviewScore: Double? = 0.0,
    val reviewCount: Int? = 0,
    val distanceInMeters: Double? = 0.0,
    val estimatedDeliveryTimeMinutes: Int? = 0,
)
