package com.elamparithi.parkypark.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.Coupon
import io.reactivex.Single

@Dao
interface CouponDao {

    @Insert
    suspend fun insert(coupon: Coupon)

    @Insert
    suspend fun insert(coupon: List<Coupon>)

    @Query("SELECT * FROM coupon WHERE coupon_id =:couponId")
    fun getGivenCouponDetails(couponId: Long): Single<Coupon>

}