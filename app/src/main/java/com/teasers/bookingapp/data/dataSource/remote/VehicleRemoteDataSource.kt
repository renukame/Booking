package com.teasers.bookingapp.data.dataSource.remote

import com.teasers.bookingapp.Logging.Tracer
import com.teasers.bookingapp.data.dataSource.remote.api.VehicleService
import java.io.IOException
import javax.inject.Inject
import com.teasers.bookingapp.data.Result
import com.teasers.bookingapp.data.model.Poi

/**
 * Data source class that handles work with MyTaxi API.
 */
class VehicleRemoteDataSource @Inject constructor(private val vehicleService: VehicleService) {

    private val TAG = VehicleRemoteDataSource::class.java.simpleName

    suspend fun getAllVehicles(
        p1Lat: Double,
        p1Lon: Double,
        p2Lat: Double,
        p2Lon: Double
    ): Result<List<Poi>> {
        try {
            val response = vehicleService.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)
            if (response.isSuccessful) {
                //Tracer.d(TAG, "Success")
                val body = response.body()
                if (body != null) {
                    return Result.Success(body.poiList)
                }
            }
            //Tracer.e(TAG, "Error getting Vehicles ${response.code()} ${response.message()}")
            return Result.Error(IOException("Error getting Vehicles ${response.code()} ${response.message()}"))
        } catch (e: Exception) {
           // Tracer.e(TAG, "Error getting Vehicles ${e.message}")
            return Result.Error(IOException("Error getting Vehicles ${e.message}"))
        }
    }
}