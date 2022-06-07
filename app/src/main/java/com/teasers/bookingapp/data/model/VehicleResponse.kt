package com.teasers.bookingapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Models a MyTaxi API VehicleResponse
 */
data class VehicleResponse(@SerializedName("poiList") val poiList: List<Poi>)