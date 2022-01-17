package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_type")
data class VehicleType(
    @ColumnInfo(name = "vehicle_type_name")
    val vehicleTypeName: String,
    @ColumnInfo(name = "icon")
    val icon: Int,
//    @ColumnInfo(name = "type")
//    val type: Int,
    @ColumnInfo(name = "price_first_hour")
    val priceFirstHour: Float,
    @ColumnInfo(name = "price_remaining_hour")
    val priceRemainingHour: Float
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vehicle_type_id")
    var vehicleTypeId: Long = 0

    override fun toString(): String {
        return vehicleTypeName
    }

}