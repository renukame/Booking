package com.teasers.bookingapp.domain.repository

import com.teasers.bookingapp.data.Result
import com.teasers.bookingapp.data.model.Poi

interface VehicleRepository {
    suspend fun getAllVehicles(
        p1Lat: Double,
        p1Lon: Double,
        p2Lat: Double,
        p2Lon: Double
    ): Result<List<Poi>>
}