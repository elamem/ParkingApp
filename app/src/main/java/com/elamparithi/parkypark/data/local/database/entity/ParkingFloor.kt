package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_floor", foreignKeys = [
        ForeignKey(
            entity = ParkingLot::class,
            parentColumns = arrayOf("parking_lot_id"),
            childColumns = arrayOf("parking_lot_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParkingFloor(
    @ColumnInfo(name = "parking_lot_id", index = true)
    val parkingLotId: Long,
    @ColumnInfo(name = "total_parking_space_count")
    val totalParkingSpaceCount: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "parking_floor_id")
    var parkingFloorId: Long = 0
}
