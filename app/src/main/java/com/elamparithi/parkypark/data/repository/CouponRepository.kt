package com.elamparithi.parkypark.data.repository

import androidx.lifecycle.LiveData
import com.elamparithi.parkypark.data.local.database.dao.CouponDao
import com.elamparithi.parkypark.data.local.database.dao.CouponUserRelDao
import com.elamparithi.parkypark.data.local.database.dao.UserDao
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CouponRepository @Inject constructor(
    private val userDao: UserDao,
    private val couponUserRelDao: CouponUserRelDao,
    private val couponDao: CouponDao
) {

    fun getCurrentActiveUserId(): LiveData<Long> {
        return userDao.getCurrentActiveUserId()
    }

    fun getCouponListForGivenUser(userId: Long, status: Int) =
        couponUserRelDao.getCouponListForGivenUser(userId, status)

    fun getCouponByGivenCode(givenCode: String) =
        userDao.getCurrentActiveUserIdSingle().flatMap { userId ->
            couponUserRelDao.getCouponByGivenCode(userId, givenCode)
        }.subscribeOn(Schedulers.io())

}