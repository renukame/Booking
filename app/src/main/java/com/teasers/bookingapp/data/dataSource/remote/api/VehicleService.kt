package com.teasers.bookingapp.data.dataSource.remote.api

import com.teasers.bookingapp.data.model.VehicleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Models the MyTaxi API.
 *
 */
interface VehicleService {

    @GET(" ")
    suspend fun getAllVehicles(
        @Query("p1Lat") p1Lat: Double,
        @Query("p1Lon") p1Lon: Double,
        @Query("p2Lat") p2Lat: Double,
        @Query("p2Lon") p2Lon: Double
    ): Response<VehicleResponse>
}