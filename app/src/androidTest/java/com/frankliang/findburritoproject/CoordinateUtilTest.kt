package com.frankliang.findburritoproject

import com.frankliang.findburritoproject.model.Coordinate
import com.frankliang.findburritoproject.util.CoordinateUtil
import org.junit.Test
import kotlin.math.abs

class CoordinateUtilTest {

    @Test
    fun test_calculateDistanceInMile_empty_value() {
        val coordinate1 = Coordinate()
        val coordinate2 = Coordinate()
        assert(CoordinateUtil.calculateDistanceInMile(coordinate1, coordinate2) == 0.0)
    }

    @Test
    fun test_calculateDistanceInMile_identical_value() {
        val coordinate1 = Coordinate(40.798368, -73.952524)
        val coordinate2 = Coordinate(40.798368, -73.952524)
        assert(CoordinateUtil.calculateDistanceInMile(coordinate1, coordinate2) == 0.0)
    }

    @Test
    fun test_calculateDistanceInMile_distinct_value() {
        val coordinate1 = Coordinate(40.798368, -73.952524)
        val coordinate2 = Coordinate(40.7134944, -74.0093015)
        //taken from https://www.movable-type.co.uk/scripts/latlong.html
        val expectValue = 6.5741072 
        val functionValue = CoordinateUtil.calculateDistanceInMile(coordinate1, coordinate2)
        assert(abs(expectValue - functionValue) < 0.01)
    }
}