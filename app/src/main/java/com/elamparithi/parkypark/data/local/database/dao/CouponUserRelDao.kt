package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.Coupon
import com.elamparithi.parkypark.data.local.database.entity.CouponUserRel
import io.reactivex.Single

@Dao
interface CouponUserRelDao {

    @Insert
    suspend fun insert(couponUserRel: CouponUserRel)

    @Query("SELECT coupon.* FROM coupon_user_rel as cur LEFT JOIN coupon ON cur.coupon_id = coupon.coupon_id WHERE cur.user_id = :userId AND cur.status = :status")
    fun getCouponListForGivenUser(userId: Long, status: Int): LiveData<List<Coupon>>

    @Query("UPDATE coupon_user_rel SET status =:status WHERE coupon_id =:currentCouponId")
    fun updateGivenCouponUserStatus(currentCouponId: Long, status: Int): Single<Int>

    @Query("SELECT coupon.* FROM coupon_user_rel as cur LEFT JOIN coupon ON cur.coupon_id = coupon.coupon_id WHERE cur.user_id = :userId AND coupon.coupon_code = :givenCode COLLATE NOCASE ")
    fun getCouponByGivenCode(userId: Long, givenCode: String): Single<Coupon>

}