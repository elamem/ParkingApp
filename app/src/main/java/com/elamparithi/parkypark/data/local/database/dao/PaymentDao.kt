package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.Payment
import io.reactivex.Single

@Dao
interface PaymentDao {

    @Insert
    fun insert(payment: Payment): Single<Long>

    @Query("UPDATE payment SET payment_status =:status WHERE payment_id =:currentBookingPaymentId")
    fun updateGivenBookingPaymentStatus(
        currentBookingPaymentId: Long,
        status: Int
    ): Single<Int>

    @Query(
        """UPDATE payment SET 
        first_hour_amount =:firstHourPayment, 
       remaining_hour_amount =:remainingHoursPayment,
        first_hour_discount =:firstHourDiscount,
remaining_hour_discount =:remainingHourDiscount WHERE payment_id =:currentBookingPaymentId"""
    )
    fun updatePaymentDetails(
        currentBookingPaymentId: Long,
        firstHourPayment: Float,
        remainingHoursPayment: Float,
        firstHourDiscount: Float,
        remainingHourDiscount: Float
    ): Single<Int>

    @Query("SELECT * FROM PAYMENT WHERE payment_id = :paymentId")
    fun getGivenPaymentIdInfo(paymentId: Long): Single<Payment>

    @Query("SELECT payment_status FROM PAYMENT WHERE payment_id = :paymentId")
    fun getCurrentPaymentStatus(paymentId: Long): LiveData<Int>


}