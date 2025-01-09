package com.study.mongo_by_kotlin.repository

import com.study.mongo_by_kotlin.domain.store.Stores
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface StoreRepository: MongoRepository<Stores, String> {

    @Query("{ 'location': { '\$nearSphere': { '\$geometry': { 'type': 'Point', 'coordinates': [?0, ?1] }, '\$maxDistance': ?2 } } }")
    fun findNearByStores(longitude: Double, latitude: Double, maxDistance: Double): List<Stores>

}