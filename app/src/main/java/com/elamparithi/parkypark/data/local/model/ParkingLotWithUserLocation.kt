package com.elamparithi.parkypark.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Ignore
import com.elamparithi.parkypark.data.local.database.entity.Location

data class ParkingLotWithUserLocation(
    @ColumnInfo(name = "parking_lot_id")
    var parkingLotId : Long,
    @Embedded
    var parkingLotLocation : Location,
    ) {
    @Ignore
    lateinit var userLocation : Location
    @Ignore
    var distance : Float = 0F
}

internal object Compare {
    fun min(a: ParkingLotWithUserLocation, b: ParkingLotWithUserLocation): ParkingLotWithUserLocation {
        return if (a.distance < b.distance) a else b
    }

    fun max(a: ParkingLotWithUserLocation, b: ParkingLotWithUserLocation): ParkingLotWithUserLocation {
        return if (a.distance > b.distance) a else b
    }
}
