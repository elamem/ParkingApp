package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "coupon_user_rel", foreignKeys = [
        ForeignKey(
            entity = Coupon::class,
            parentColumns = arrayOf("coupon_id"),
            childColumns = arrayOf("coupon_id"),
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
data class CouponUserRel(
    @ColumnInfo(name = "coupon_id", index = true)
    val couponId: Long,
    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,
    @ColumnInfo(name = "status")
    val status: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "coupon_user_rel_id")
    var couponUserRelId: Long = 0
}
