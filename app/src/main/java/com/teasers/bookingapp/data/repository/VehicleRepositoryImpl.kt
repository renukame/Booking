package com.teasers.bookingapp.data.repository

import com.teasers.bookingapp.Logging.Tracer
import com.teasers.bookingapp.data.Result
import com.teasers.bookingapp.data.dataSource.remote.VehicleRemoteDataSource
import com.teasers.bookingapp.data.model.Poi
import com.teasers.bookingapp.domain.repository.VehicleRepository
import javax.inject.Inject

/**
 * Manage saving and retrieving data from cache or network.
 */
class VehicleRepositoryImpl @Inject constructor(private val vehicleRemoteDataSource: VehicleRemoteDataSource) :
    VehicleRepository {

    private val TAG = VehicleRepositoryImpl::class.java.simpleName
    private val cache = mutableMapOf<String, List<Poi>>()

    override suspend fun getAllVehicles(
        p1Lat: Double,
        p1Lon: Double,
        p2Lat: Double,
        p2Lon: Double
    ): Result<List<Poi>> {
        val key = "$p1Lat,$p1Lon,$p2Lat,$p2Lon"
        return if (cache.containsKey(key)) {
            //Tracer.d(TAG, "Response from cache")
            Result.Success(cache[key]!!)
        } else {
            val response = vehicleRemoteDataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)
            //Tracer.d(TAG, "Response from Network")
            if (response is Result.Success) {
                cache[key] = response.data
            }
            response
        }
    }
}


