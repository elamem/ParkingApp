package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupon")
data class Coupon(
    @ColumnInfo(name = "coupon_name")
    val couponName: String,
    @ColumnInfo(name = "coupon_code")
    val couponCode: String,
    @ColumnInfo(name = "discount_percent_first_hour")
    val discountPercentFirstHour: Int,
    @ColumnInfo(name = "discount_percent_remaining_hour")
    val discountPercentRemainingHour: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "coupon_id")
    var couponId: Long = 0
}
