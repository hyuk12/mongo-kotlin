package com.study.mongo_by_kotlin.service

import com.study.mongo_by_kotlin.domain.store.Stores
import com.study.mongo_by_kotlin.dto.StoreAdminResponse
import com.study.mongo_by_kotlin.dto.StoreRequest
import com.study.mongo_by_kotlin.dto.StoreResponse
import com.study.mongo_by_kotlin.repository.StoreRepository
import com.study.mongo_by_kotlin.service.StoreService.Companion.AVERAGE_SPEED_KM_PER_HOUR
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.*

private val logger = LoggerFactory.getLogger(StoreService::class.java)
@Service
class StoreService(
    private val storeRepository: StoreRepository,
) {
    companion object {
        const val AVERAGE_SPEED_KM_PER_HOUR = 60.0 // 오토바이 평균 속도
    }

    fun findNearByStores(longitude: Double, latitude: Double, maxDistanceKm: Double): List<StoreResponse> {
        logger.info("findNearByStores called with longitude: $longitude, latitude: $latitude, maxDistanceKm: $maxDistanceKm")
        val stores = storeRepository.findNearByStores(longitude, latitude, maxDistanceKm * 1000)

        logger.info("Found stores: ${stores.size}")
        return stores.map { store ->
            val distanceInMeters = calculateDistance(latitude, longitude, store.location)
            logger.info("Calculated distance for ${store.name}: $distanceInMeters meters")
            val calculateDeliveryTime = calculateDeliveryTime(distanceInMeters)
            logger.info("Calculated deliveryTime for ${store.name}: $calculateDeliveryTime")
            StoreResponse(
                id = store.id,
                name = store.name,
                location = store.location,
                reviewScore = store.reviewScore,
                reviewCount = store.reviewCount,
                distanceInMeters = distanceInMeters,
                estimatedDeliveryTimeMinutes = calculateDeliveryTime,
            )
        }
    }

    @Transactional
    fun createStore(request: Stores): StoreAdminResponse {
        val store = storeRepository.save(request)
        return StoreAdminResponse(
            id = store.id,
        )
    }

}

private fun calculateDistance(lat1: Double, lon1: Double, location: GeoJsonPoint): Double {
    val lat2 = location.coordinates[1]
    val lon2 = location.coordinates[0]

    val R = 6371e3 // 지구의 반경 (미터)

    val lat1Rad = lat1.toRadians()
    val lat2Rad = lat2.toRadians()
    val deltaLat = (lat2 - lat1).toRadians()
    val deltaLon = (lon2 - lon1).toRadians()

    val a = sin(deltaLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c // 미터 단위의 거리
}

// 확장 함수로 도(degree)를 라디안으로 변환
private fun Double.toRadians(): Double = this * PI / 180.0

private fun calculateDeliveryTime(distanceInMeters: Double): Int {
    val distanceInKm = distanceInMeters / 1000
    val timeInHours = distanceInKm / AVERAGE_SPEED_KM_PER_HOUR
    return (timeInHours * 60).toInt().coerceAtLeast(1) // 최소 1분으로 설정
}