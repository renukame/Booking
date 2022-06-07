package com.teasers.bookingapp.Logging

import android.util.Log

/**
 * Logging Class
 */
object Tracer {
    private const val appName = "Booking"

    fun v(tag: String?, msg: String) {
        Log.v("$appName $tag", msg)
    }

    fun d(tag: String?, msg: String) {
        Log.d("$appName $tag", msg)
    }

    fun i(tag: String?, msg: String) {
        Log.i("$appName $tag", msg)
    }

    fun w(tag: String?, msg: String) {
        Log.w("$appName $tag", msg)
    }

    fun e(tag: String?, msg: String) {
        Log.e("$appName $tag", msg)
    }
}