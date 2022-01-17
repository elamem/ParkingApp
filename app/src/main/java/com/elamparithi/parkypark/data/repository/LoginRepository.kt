package com.elamparithi.parkypark.data.repository

import com.elamparithi.parkypark.data.local.database.dao.CouponUserRelDao
import com.elamparithi.parkypark.data.local.database.dao.UserDao
import com.elamparithi.parkypark.data.local.database.entity.CouponUserRel
import com.elamparithi.parkypark.data.local.database.entity.User
import com.elamparithi.parkypark.data.local.prefs.UserPrefs
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val userDao: UserDao,
    private val couponUserRelDao: CouponUserRelDao,
    private val userPrefs: UserPrefs
) {

    suspend fun insertUserDetails(user: User): Long {
        val userId = userDao.insert(user)
        userPrefs.setUserId(userId)
        return userId
    }

    suspend fun clearUserDetails() {
        userDao.clearUserDetails()
    }

    suspend fun insertUserCoupon(couponUserRel: CouponUserRel) {
        couponUserRelDao.insert(couponUserRel)
    }

}