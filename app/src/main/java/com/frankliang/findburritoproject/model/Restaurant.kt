package com.frankliang.findburritoproject.model

import android.os.Parcel
import android.os.Parcelable
import com.frankliang.findburritoproject.BurritoPlaceListQuery
import kotlinx.parcelize.Parcelize

@Parcelize
class Restaurant(
    val id: String?,
    val name: String?,
    val rating: Double?,
    val reviewCount: Int?,
    val phone: String?,
    val priceLevel: String?,
    val distance: Double?,
    private val latitude: Double?,
    private val longitude: Double?,
    val isOpen: Boolean?,
    val streetAddress: String?
): Parcelable {
    constructor(restaurant: BurritoPlaceListQuery.Business) : this(
        restaurant.id,
        restaurant.name,
        restaurant.rating,
        restaurant.review_count,
        restaurant.display_phone,
        restaurant.price,
        restaurant.distance,
        restaurant.coordinates?.latitude,
        restaurant.coordinates?.longitude,
        if(restaurant.hours?.size != 0) restaurant.hours?.get(0)?.is_open_now else false,
        restaurant.location?.address1
    )

    fun getCoordinate(): Coordinate {
        return Coordinate(latitude, longitude)
    }

    override fun toString(): String {
        return "Restaurant(id=$id, name=$name, rating=$rating, reviewCount=$reviewCount, phone=$phone, priceLevel=$priceLevel, distance=$distance, isOpen=$isOpen, streetAddress=$streetAddress)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Restaurant) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (rating != other.rating) return false
        if (reviewCount != other.reviewCount) return false
        if (phone != other.phone) return false
        if (priceLevel != other.priceLevel) return false
        if (distance != other.distance) return false
        if (longitude != other.longitude) return false
        if (latitude != other.latitude) return false
        if (isOpen != other.isOpen) return false
        if (streetAddress != other.streetAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (reviewCount ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (priceLevel?.hashCode() ?: 0)
        result = 31 * result + (distance?.hashCode() ?: 0)
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        result = 31 * result + (isOpen?.hashCode() ?: 0)
        result = 31 * result + (streetAddress?.hashCode() ?: 0)
        return result
    }


}