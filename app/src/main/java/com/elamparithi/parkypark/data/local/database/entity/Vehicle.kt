package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle",
    foreignKeys = [
        ForeignKey(
        entity = VehicleType::class,
        parentColumns = arrayOf("vehicle_type_id"),
        childColumns = arrayOf("vehicle_type_id"),
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Vehicle(
    @ColumnInfo(name = "vehicle_type_id", index = true)
    val vehicleTypeId: Long,
    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,
    @ColumnInfo(name = "plate")
    val plate: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vehicle_id")
    var vehicleId: Long = 0
}
