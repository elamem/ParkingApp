package com.elamparithi.parkypark.data.local.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class VehicleWithVehicleType(
    @ColumnInfo(name = "vehicle_type_id")
    val vehicleTypeId: Long,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "plate")
    val plate: String,
    @ColumnInfo(name = "vehicle_type_name")
    val typeName: String,
    @ColumnInfo(name = "icon")
    val icon: Int,
    @ColumnInfo(name = "price_first_hour")
    val priceFirstHour: Int,
    @ColumnInfo(name = "price_remaining_hour")
    val priceRemainingHour: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vehicle_id")
    var vehicleId: Long = 0
}