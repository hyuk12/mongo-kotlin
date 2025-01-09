package com.study.mongo_by_kotlin.controller

import com.study.mongo_by_kotlin.dto.StoreAdminResponse
import com.study.mongo_by_kotlin.dto.StoreRequest
import com.study.mongo_by_kotlin.dto.StoreResponse
import com.study.mongo_by_kotlin.dto.toEntity
import com.study.mongo_by_kotlin.service.StoreService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/store"])
class StoreController(
    private val storeService: StoreService,
) {

    @PostMapping
    fun createStore(
        @RequestBody request: StoreRequest,
    ): ResponseEntity<StoreAdminResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(storeService.createStore(request.toEntity()))
    }

    @GetMapping
    fun findNearByStores(
        @RequestParam longitude: Double,
        @RequestParam latitude: Double,
        @RequestParam maxDistanceKm: Double
    ): List<StoreResponse> {
        return storeService.findNearByStores(longitude, latitude, maxDistanceKm)
    }
}