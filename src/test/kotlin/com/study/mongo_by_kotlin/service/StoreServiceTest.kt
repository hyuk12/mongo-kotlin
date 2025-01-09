package com.study.mongo_by_kotlin.service

import com.study.mongo_by_kotlin.domain.store.Stores
import com.study.mongo_by_kotlin.dto.StoreResponse
import com.study.mongo_by_kotlin.repository.StoreRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.data.mongodb.core.geo.GeoJsonPoint


class StoreServiceTest {

 private lateinit var storeService: StoreService
 private lateinit var storeRepository: StoreRepository

 @BeforeEach
 fun setup() {
  storeRepository = mock(StoreRepository::class.java)
  storeService = StoreService(storeRepository)
 }

 @Test
 fun testFindNearByStores() {
  val testLongitude = 127.027610
  val testLatitude = 37.498095
  val maxDistanceKm = 5.0  // 검색 반경을 10km로 확장

  val mockStores = listOf(
   Stores("1", "Store 1", image = "", location = GeoJsonPoint(127.028610, 37.499095), reviewScore = 4.5, reviewCount = 100),
   Stores("2", "Store 2", image = "", location = GeoJsonPoint(127.026610, 37.497095), reviewScore = 4.0, reviewCount = 80),
   Stores("3", "Store 3", image = "", location = GeoJsonPoint(127.077610, 37.548095), reviewScore = 4.2, reviewCount = 90)  // 약 8km 떨어진 위치

  )

  `when`(storeRepository.findNearByStores(testLongitude, testLatitude, maxDistanceKm * 1000))
   .thenReturn(mockStores)

  val result = storeService.findNearByStores(testLongitude, testLatitude, maxDistanceKm)

  assertEquals(3, result.size)
  assertTrue(result.all { it is StoreResponse })
  assertTrue(result.all { it.distanceInMeters > 0 })
  assertTrue(result.all { it.estimatedDeliveryTimeMinutes > 0 })

  // 가장 먼 가게(Store 3)에 대한 검증
  val farthestStore = result.maxBy { it.distanceInMeters }
  assertTrue(farthestStore.distanceInMeters > 1000)  // 1km 이상
  assertTrue(farthestStore.estimatedDeliveryTimeMinutes > 5)  // 5분 이상

  verify(storeRepository).findNearByStores(testLongitude, testLatitude, maxDistanceKm * 1000)
 }

}