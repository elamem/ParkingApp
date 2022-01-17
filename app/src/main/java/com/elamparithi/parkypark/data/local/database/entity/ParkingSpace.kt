package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_space", foreignKeys = [
        ForeignKey(
            entity = ParkingFloor::class,
            parentColumns = arrayOf("parking_floor_id"),
            childColumns = arrayOf("parking_floor_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VehicleType::class,
            parentColumns = arrayOf("vehicle_type_id"),
            childColumns = arrayOf("vehicle_type_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParkingSpace(
    @ColumnInfo(name = "parking_floor_id", index = true)
    val parkingFloorId: Long,
    @ColumnInfo(name = "vehicle_type_id", index = true)
    val vehicleTypeId: Long,
    @ColumnInfo(name = "status")
    val status: Int
    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "parking_space_id")
    var parkingSpaceId: Long = 0
}
