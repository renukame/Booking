package com.teasers.bookingapp.domain.usecases

import com.teasers.bookingapp.data.Result
import com.teasers.bookingapp.data.model.Poi
import com.teasers.bookingapp.domain.repository.VehicleRepository
import javax.inject.Inject

/**
 * Use case that gets all vehicle for bounds of location
 */
class GetAllVehicleUseCase @Inject constructor(private val vehicleRepository: VehicleRepository) {
    suspend operator fun invoke(
        p1Lat: Double,
        p1Lon: Double,
        p2Lat: Double,
        p2Lon: Double
    ): Result<List<Poi>> {
        return vehicleRepository.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)
    }
}