package com.teasers.bookingapp.data.repository


import com.teasers.bookingapp.data.dataSource.remote.VehicleRemoteDataSource
import com.teasers.bookingapp.data.dataSource.remote.api.VehicleService
import com.teasers.bookingapp.data.model.Coordinate
import com.teasers.bookingapp.data.model.Poi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import com.teasers.bookingapp.data.Result
import io.mockk.coVerify
import java.io.IOException

class VehicleRepositoryImplTest {
    private val dataSource: VehicleRemoteDataSource = mockk()
    private val repository = VehicleRepositoryImpl(dataSource)

    private val p1Lat = 53.694865
    private val p1Lon = 9.757589
    private val p2Lat = 53.394655
    private val p2Lon = 10.099891

    private val coordinate = Coordinate(53.46036882190762, 9.909716434648558)
    private val poi = Poi(439670, coordinate, "POOLING", 344.19529122029735)
    private val poiList = listOf(poi)

    private val cache = mutableMapOf<String, List<Poi>>()

    @Test
    fun getAllVehicles_whenLoadSucceeded_Cached() = runBlocking {
        // Given that a load has been performed successfully and data cached
        coEvery { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) } returns (Result.Success(
            poiList
        ))

        repository.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        coVerify(exactly = 1) { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) }
        // When getting result from cache
        val result = repository.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        // Then it is successfully retrieved
        assertNotNull(result)
        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) }

    }

    @Test
    fun getAllVehicles_whenCached() = runBlocking {
        // Given that a load has been performed successfully and data cached
        coEvery { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) } returns (Result.Success(
            poiList
        ))

        repository.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        coEvery { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, 10.09) } returns (Result.Success(
            poiList
        ))

        // When getting result from cache
        val result = repository.getAllVehicles(p1Lat, p1Lon, p2Lat, 10.09)

        // Then it is successfully retrieved
        assertNotNull(result)
        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) }
        coVerify(exactly = 1) { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, 10.09) }
    }

    @Test
    fun getAllVehicles_whenLoadFailed_DataNotCached() = runBlocking {
        // when api fails so no data is cached
        coEvery { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) } returns (Result.Error(
            IOException("error")
        ))

        repository.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        // When getting vehicles
        val result = repository.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        // Then error is returned
        assertNotNull(result)
        assertTrue(result is Result.Error)
        coVerify(exactly = 2) { dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) }
    }
}