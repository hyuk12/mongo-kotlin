package com.study.mongo_by_kotlin.domain.store

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "storesTree")
data class Stores(
    @Id
    val id: String? = null,
    val name: String,
    val address: String,
    val city: String,
    val zipCode: String,
    val image: String? = "default.png",
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    @Field(name = "location")
    val location: GeoJsonPoint,
    val reviewScore: Double? = 0.0,
    val reviewCount: Int? = 0,
)

