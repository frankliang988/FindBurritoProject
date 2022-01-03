package com.frankliang.findburritoproject.model

data class Coordinate(val latitude: Double? = 0.0, val longitude: Double? = 0.0) {
    override fun toString(): String {
        return "Coordinate(latitude=$latitude, longitude=$longitude)"
    }
}
