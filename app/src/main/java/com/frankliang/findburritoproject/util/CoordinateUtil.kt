package com.frankliang.findburritoproject.util

import android.location.Location
import android.util.Log
import com.frankliang.findburritoproject.model.Coordinate

object CoordinateUtil {
    private const val METER_TO_MILE = 0.000621371
    private const val THRESHOLD_IN_MILES = 1

    fun hasUserMovedPassThreshold(c1: Coordinate, c2: Coordinate): Boolean {
        return calculateDistanceInMile(c1, c2) >= THRESHOLD_IN_MILES
    }

    fun calculateDistanceInMile(c1: Coordinate, c2: Coordinate): Double {
        val result = FloatArray(3)
        Location.distanceBetween(
            c1.latitude ?: 0.0,
            c1.longitude ?: 0.0,
            c2.latitude ?: 0.0,
            c2.longitude ?: 0.0, result
        )
        Log.e("TEST", "distance: ${result[0] * METER_TO_MILE} miles")

        return result[0] * METER_TO_MILE
    }
}