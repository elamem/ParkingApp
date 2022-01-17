package com.elamparithi.parkypark.di

import android.app.Application
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.local.prefs.UserPrefs
import com.elamparithi.parkypark.data.repository.HomeRepository
import com.elamparithi.parkypark.data.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideHomeRepository(
        vehicleTypeDao: VehicleTypeDao,
        vehicleDao: VehicleDao,
        userDao: UserDao,
        bookingDao: BookingDao,
        parkingLotDao: ParkingLotDao,
        application: Application
    ) = HomeRepository(vehicleTypeDao, vehicleDao, userDao, bookingDao, parkingLotDao, application)


    @Provides
    fun provideLoginRepository(
        userDao: UserDao,
        userPrefs: UserPrefs,
        couponUserRelDao: CouponUserRelDao
    ) = LoginRepository(userDao, couponUserRelDao, userPrefs)

}