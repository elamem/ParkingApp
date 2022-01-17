package com.elamparithi.parkypark.di

import android.app.Application
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.remote.NetworkService
import com.elamparithi.parkypark.data.repository.BookingRepository
import com.elamparithi.parkypark.data.repository.CouponRepository
import com.elamparithi.parkypark.data.repository.PickUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Provides
    fun provideBookingRepository(
        userDao: UserDao,
        parkingLotDao: ParkingLotDao,
        bookingDao: BookingDao,
        paymentDao: PaymentDao,
        parkingSpaceDao: ParkingSpaceDao,
        vehicleDao: VehicleDao,
        vehicleTypeDao: VehicleTypeDao,
        couponUserRelDao: CouponUserRelDao,
        networkService: NetworkService,
        application: Application
    ) = BookingRepository(
        userDao,
        parkingLotDao,
        bookingDao,
        paymentDao,
        parkingSpaceDao,
        vehicleDao,
        vehicleTypeDao,
        couponUserRelDao,
        networkService,
        application
    )

    @Provides
    fun provideCouponRepository(
        userDao: UserDao,
        couponUserRelDao: CouponUserRelDao,
        couponDao: CouponDao
    ) = CouponRepository(userDao, couponUserRelDao, couponDao)

    @Provides
    fun providePickUpRepository(
        bookingDao: BookingDao,
        vehicleTypeDao: VehicleTypeDao,
        parkingSpaceDao: ParkingSpaceDao,
        paymentDao: PaymentDao,
        couponDao: CouponDao
    ) = PickUpRepository(bookingDao, vehicleTypeDao, parkingSpaceDao, paymentDao, couponDao)


}