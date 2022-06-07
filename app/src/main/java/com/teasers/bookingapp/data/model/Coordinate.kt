package com.teasers.bookingapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Models a MyTaxi API Coordinate
 */
data class Coordinate(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
