package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment"
)
data class Payment (
    @ColumnInfo(name = "first_hour_amount")
    val firstHourAmount: Float,
    @ColumnInfo(name = "remaining_hour_amount")
    val remainingHourAmount: Float,
    @ColumnInfo(name = "first_hour_discount")
    val firstHourDiscount: Float,
    @ColumnInfo(name = "remaining_hour_discount")
    val remainingHourDiscount: Float,
    @ColumnInfo(name = "reservation_fee")
    val reservationFee: Float = 0F,
    @ColumnInfo(name = "cancellation_fee")
    val cancellationFee: Float = 0F,
    @ColumnInfo(name = "payment_status")
    val paymentStatus: Int,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    var paymentId :Long = 0
    @Ignore
    var total :Float = 0F
}
