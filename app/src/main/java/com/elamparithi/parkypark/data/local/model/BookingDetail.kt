package com.elamparithi.parkypark.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.elamparithi.parkypark.data.local.database.entity.Location


data class BookingDetail (
    @ColumnInfo(name = "booking_id")
    val bookingId :Long,
    @ColumnInfo(name = "payment_id")
    val paymentId :Long,
    @ColumnInfo(name = "parking_type")
    val parkingType :Int,
    @ColumnInfo(name = "booking_status")
    val bookingStatus: Int,
    @ColumnInfo(name = "vehicle_id")
    val vehicleId :Long,
    @ColumnInfo(name = "plate")
    val plateNo :String,
    @ColumnInfo(name = "vehicle_type_id")
    val vehicleTypeId :Long,
    @ColumnInfo(name = "icon")
    val vehicleIcon :Int,
    @ColumnInfo(name = "vehicle_type_name")
    val vehicleTypeName :String,
    @ColumnInfo(name = "price_first_hour")
    val priceFirstHour: Float,
    @ColumnInfo(name = "price_remaining_hour")
    val priceRemainingHour: Float,
    @ColumnInfo(name = "parking_space_id")
    val parkingSpaceId : Long,
    @ColumnInfo(name = "parking_floor_id")
    val parkingFloorId : Long,
    @ColumnInfo(name = "parking_lot_id")
    val parkingLotId : Long,
    @ColumnInfo(name = "distance")
    val distance : Float,
    @Embedded
    var parkingLotLocation : Location,
    @ColumnInfo(name = "coupon_id")
    val couponId: Long,
    @ColumnInfo(name = "coupon_name")
    val name: String?,
    @ColumnInfo(name = "coupon_code")
    val code: String?,
    @ColumnInfo(name = "discount_percent_first_hour")
    val discountPercentFirstHour: Int?,
    @ColumnInfo(name = "discount_percent_remaining_hour")
    val discountPercentRemainingHour: Int?
)