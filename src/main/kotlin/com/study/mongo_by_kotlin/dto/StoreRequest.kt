package com.study.mongo_by_kotlin.dto

import com.study.mongo_by_kotlin.domain.store.Stores
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class StoreRequest(
    val name: String,
    val address: String,
    val city: String,
    val zipCode: String,
    val latitude: Double,
    val longitude: Double,
)

fun StoreRequest.toEntity() = Stores(
    name = name,
    address = address,
    city = city,
    zipCode = zipCode,
    location = convertToGeoJsonPoint(longitude, latitude)  // 수정: (위도, 경도) 순서
)

fun convertToGeoJsonPoint(longitude: Double, latitude: Double) = GeoJsonPoint(longitude, latitude)
