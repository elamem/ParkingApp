package com.elamparithi.parkypark.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.local.database.entity.*

@Database(
    entities = [
        Booking::class,
        Coupon::class,
        CouponUserRel::class,
        ParkingFloor::class,
        ParkingLot::class,
        ParkingSpace::class,
        User::class,
        Vehicle::class,
        VehicleType::class,
        Payment::class,
    ], version = 1, exportSchema = false
)

abstract class DatabaseService : RoomDatabase() {

    abstract fun bookingDao(): BookingDao
    abstract fun couponDao(): CouponDao
    abstract fun couponUserRelDao(): CouponUserRelDao
    abstract fun parkingFloorDao(): ParkingFloorDao
    abstract fun parkingLotDao(): ParkingLotDao
    abstract fun parkingSpaceDao(): ParkingSpaceDao
    abstract fun userDao(): UserDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun vehicleTypeDao(): VehicleTypeDao
    abstract fun paymentDao(): PaymentDao
}