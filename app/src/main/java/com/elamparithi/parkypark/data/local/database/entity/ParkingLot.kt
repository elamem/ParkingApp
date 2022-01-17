package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "parking_lot")
data class ParkingLot(
    @ColumnInfo(name = "name")
    val name: String,
    @Embedded
    val location: Location,
    @ColumnInfo(name = "floor_count")
    val floorCount: Int,
    @ColumnInfo(name = "parking_space_per_floor")
    val parkingSpacePerFloor: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "parking_lot_id")
    var parkingLotId: Long = 0
}
