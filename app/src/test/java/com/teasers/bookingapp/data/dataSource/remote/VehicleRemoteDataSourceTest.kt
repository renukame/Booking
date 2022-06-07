package com.teasers.bookingapp.data.dataSource.remote


import com.teasers.bookingapp.data.Result
import com.teasers.bookingapp.data.dataSource.remote.api.VehicleService
import com.teasers.bookingapp.data.model.Coordinate
import com.teasers.bookingapp.data.model.Poi
import com.teasers.bookingapp.data.model.VehicleResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class VehicleRemoteDataSourceTest {

    private val service: VehicleService = mockk()
    private val dataSource = VehicleRemoteDataSource(service)

    private val coordinate = Coordinate(53.46036882190762, 9.909716434648558)
    private val poi = Poi(439670, coordinate, "POOLING", 344.19529122029735)
    private val poList = listOf(poi)

    private val p1Lat = 53.694865
    private val p1Lon = 9.757589
    private val p2Lat = 53.394655
    private val p2Lon = 10.099891

    private val errorResponseBody = "Error".toResponseBody("".toMediaTypeOrNull())

    @Test
    fun getAllVehicles_success() = runBlocking {

        // Given that the service responds with success
        val apiResult = Response.success(VehicleResponse(poList))

        coEvery { service.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) } returns (apiResult)


        // When requesting the all vehicles in bounds of hamburg
        val result = dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        // Then there's one request to the service
        coVerify(exactly = 1) { service.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) }

        assertEquals(Result.Success<Poi>(listOf(poi)), result)

    }

    @Test
    fun getAllVehicles_ThrowError() = runBlocking {
        // Given that the service responds with error
        val apiResult = Response.error<VehicleResponse>(
            400,
            errorResponseBody
        )
        coEvery { service.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) } returns (apiResult)

        // When requesting the all vehicles in bounds of hamburg
        val result = dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        // Then error is returned
        assertTrue(result is Result.Error)
    }

    @Test
    fun getAllVehicles_ThrowException() = runBlocking {

        // Given that the service throws an exception
        coEvery { service.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon) }.throws(UnknownHostException())

        // When requesting the all vehicles in bounds of hamburg
        val result = dataSource.getAllVehicles(p1Lat, p1Lon, p2Lat, p2Lon)

        // Then error is returned
        assertTrue(result is Result.Error)
    }

}