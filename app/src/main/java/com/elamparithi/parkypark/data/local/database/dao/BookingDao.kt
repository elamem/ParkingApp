package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.data.local.model.BookingDetail
import com.elamparithi.parkypark.data.local.model.BookingHistory
import com.elamparithi.parkypark.data.local.model.BookingStatusDetails
import io.reactivex.Single

@Dao
abstract class BookingDao {

    @Insert
    abstract fun insert(booking: Booking): Single<Long>

    @Query(
        """SELECT 
        bk.booking_id,
        bk.payment_id,
        bk.parking_type,
        bk.booking_status,
        bk.distance,
        vh.vehicle_id,
        vh.plate,
        vt.vehicle_type_id,
        vt.icon,
        vt.vehicle_type_name,
        vt.price_first_hour,
        vt.price_remaining_hour,
        ps.parking_space_id, 
        pf.parking_floor_id, 
        pl.parking_lot_id,
        pl.latitude, 
        pl.longitude,
        bk.coupon_id,
        cp.coupon_name,
        cp.coupon_code,
        cp.discount_percent_first_hour,
        cp.discount_percent_remaining_hour
        FROM booking AS bk 
        LEFT JOIN coupon AS cp ON cp.coupon_id = bk.coupon_id
        LEFT JOIN vehicle AS vh ON bk.vehicle_id = vh.vehicle_id 
        LEFT JOIN vehicle_type AS vt ON vh.vehicle_type_id = vt.vehicle_type_id
        LEFT JOIN parking_space AS ps ON bk.parking_space_id = ps.parking_space_id 
        LEFT JOIN parking_floor AS pf ON ps.parking_floor_id = pf.parking_floor_id 
        LEFT JOIN parking_lot as pl ON pf.parking_lot_id = pl.parking_lot_id WHERE bk.booking_status = :statusInProgress OR bk.booking_status = :statusActive"""
    )
    abstract fun getCurrentlyActiveBookingFullDetails(
        statusInProgress: Int,
        statusActive: Int
    ): LiveData<BookingDetail>


    @Query("""SELECT booking_id, booking_status FROM booking WHERE parking_type =:parkingType AND booking_status = :statusInProgress OR booking_status = :statusActive""")
    abstract fun getCurrentlyActiveOrInProgressBookingDetails(
        parkingType: Int,
        statusInProgress: Int,
        statusActive: Int
    ): LiveData<BookingStatusDetails>

    @Query("UPDATE booking SET booking_status =:status WHERE booking_id =:currentBookingId")
    abstract fun updateGivenBookingStatus(currentBookingId: Long, status: Int): Single<Int>

    @Query("UPDATE booking SET booking_status =:status, in_time = :inTime WHERE booking_id =:currentBookingId")
    abstract fun activateBookingStatus(
        currentBookingId: Long,
        status: Int,
        inTime: Long
    ): Single<Int>


    @Query(
        """SELECT 
        bk.booking_id,
        bk.parking_type,
        bk.booking_status,
        vh.vehicle_id,
        vh.plate,
        vt.vehicle_type_id,
        vt.icon,
        vt.vehicle_type_name,
        ps.parking_space_id, 
        pf.parking_floor_id, 
        pl.parking_lot_id,
        bk.coupon_id,
        cp.coupon_name,
        cp.coupon_code,
        bk.in_time,
        bk.out_time,
        py.payment_id,
        py.payment_status
        FROM booking AS bk 
        LEFT JOIN coupon AS cp ON cp.coupon_id = bk.coupon_id
        LEFT JOIN vehicle AS vh ON bk.vehicle_id = vh.vehicle_id 
        LEFT JOIN user ON user.user_id = vh.user_id 
        LEFT JOIN payment as py ON py.payment_id = bk.payment_id 
        LEFT JOIN vehicle_type AS vt ON vh.vehicle_type_id = vt.vehicle_type_id
        LEFT JOIN parking_space AS ps ON bk.parking_space_id = ps.parking_space_id 
        LEFT JOIN parking_floor AS pf ON ps.parking_floor_id = pf.parking_floor_id 
        LEFT JOIN parking_lot as pl ON pf.parking_lot_id = pl.parking_lot_id WHERE user.user_id = :userId and vt.vehicle_type_id BETWEEN :vehicleTypeFrom AND :vehicleTypeTo"""
    )
    abstract fun getAvailableBookingList(
        userId: Long,
        vehicleTypeFrom: Long,
        vehicleTypeTo: Long
    ): LiveData<List<BookingHistory>>

    @Query("UPDATE booking SET out_time =:outTime WHERE booking_id =:bookingId")
    abstract fun updateOutTime(outTime: Long, bookingId: Long)

    @Query("""SELECT out_time FROM booking WHERE booking_id=:bookingId""")
    abstract fun getCurrentBookingOutTime(bookingId: Long): LiveData<Long>

    @Query("""SELECT booking_status FROM booking WHERE booking_id=:bookingId""")
    abstract fun getCurrentBookingStatus(bookingId: Long): LiveData<Int>

}