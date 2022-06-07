package com.teasers.bookingapp.data

import com.teasers.bookingapp.data.model.Poi

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: List<Poi>) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}