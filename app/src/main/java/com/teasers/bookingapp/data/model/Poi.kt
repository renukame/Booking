package com.teasers.bookingapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Models a MyTaxi API Poi
 */
data class Poi(
    @SerializedName("id")
    val id: Int,
    @SerializedName("coordinate")
    val coordinate: Coordinate,
    @SerializedName("fleetType")
    val fleetType: String,
    @SerializedName("heading")
    val heading: Double
)
