package com.elamparithi.parkypark.data.repository

import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.local.database.entity.Payment
import com.elamparithi.parkypark.data.local.model.BookingHistory
import com.elamparithi.parkypark.utils.Common
import com.elamparithi.parkypark.utils.Constants
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PickUpRepository @Inject constructor(
    private val bookingDao: BookingDao,
    private val vehicleTypeDao: VehicleTypeDao,
    private val parkingSpaceDao: ParkingSpaceDao,
    private val paymentDao: PaymentDao,
    private val couponDao: CouponDao
) {

    fun calculatePayment(bookingDetail: BookingHistory): Single<Payment> {
        return vehicleTypeDao.getGivenVehicleTypeDetails(bookingDetail.vehicleId)
            .flatMap { vehicleType ->
                val outTime = Common.getCurrentTimeStamp()
                bookingDao.updateOutTime(outTime, bookingDetail.bookingId)
                Timber.e("In Time ${bookingDetail.inTime} out Time $outTime ")
                val totalHours: Int = Common.getTotalHours(bookingDetail.inTime, outTime)
                Timber.e("total hours $totalHours")
                val firstHourPayment = vehicleType.priceFirstHour
                var firstHourDiscount = 0F
                var remainingHoursPayment = 0F
                var remainingHourDiscount = 0F
                if (totalHours > 1) {
                    remainingHoursPayment = vehicleType.priceRemainingHour * (totalHours - 1)
                }
                if (bookingDetail.couponId > 0) {
                    couponDao.getGivenCouponDetails(bookingDetail.couponId).flatMap { coupon ->
                        firstHourDiscount = Common.calculateDiscountPrice(
                            firstHourPayment,
                            coupon.discountPercentFirstHour
                        )
                        if (totalHours > 1) {
                            remainingHourDiscount = Common.calculateDiscountPrice(
                                remainingHoursPayment,
                                coupon.discountPercentRemainingHour
                            )
                        }
                        paymentDao.updatePaymentDetails(
                            bookingDetail.paymentId,
                            firstHourPayment,
                            remainingHoursPayment,
                            firstHourDiscount,
                            remainingHourDiscount
                        )
                    }
                } else {
                    paymentDao.updatePaymentDetails(
                        bookingDetail.paymentId,
                        firstHourPayment,
                        remainingHoursPayment,
                        firstHourDiscount,
                        remainingHourDiscount
                    )
                }
            }
            .flatMap { updatedRowCount ->
                if (updatedRowCount > 0) {
                    paymentDao.getGivenPaymentIdInfo(bookingDetail.paymentId)
                } else {
                    Single.just(null)
                }
            }.map { currentBookingPayment ->
                var total = currentBookingPayment.firstHourAmount +
                        currentBookingPayment.remainingHourAmount +
                        currentBookingPayment.cancellationFee +
                        currentBookingPayment.reservationFee
                total -= (currentBookingPayment.firstHourDiscount + currentBookingPayment.remainingHourDiscount)
                currentBookingPayment.total = total
                currentBookingPayment
            }
            .subscribeOn(Schedulers.io())

    }


    fun releaseTheVehicle(bookingDetail: BookingHistory): Single<List<Int>> {
        return Single.merge(
            bookingDao.updateGivenBookingStatus(
                bookingDetail.bookingId,
                Constants.BOOKING_STATUS_COMPLETED
            )
                .subscribeOn(Schedulers.io()),
            parkingSpaceDao.updateGivenParkingSpaceStatus(
                bookingDetail.parkingSpaceId,
                Constants.PARKING_SPACE_STATUS_FREE
            )
                .subscribeOn(Schedulers.io()),
            paymentDao.updateGivenBookingPaymentStatus(
                bookingDetail.paymentId,
                Constants.PAYMENT_STATUS_PAID
            )
                .subscribeOn(Schedulers.io()),
        ).toList()
    }

    fun getGivenPaymentIdInfo(paymentId: Long) =
        paymentDao.getGivenPaymentIdInfo(paymentId).map { currentBookingPayment ->
            var total = currentBookingPayment.firstHourAmount +
                    currentBookingPayment.remainingHourAmount +
                    currentBookingPayment.cancellationFee +
                    currentBookingPayment.reservationFee
            total -= (currentBookingPayment.firstHourDiscount + currentBookingPayment.remainingHourDiscount)
            currentBookingPayment.total = total
            currentBookingPayment
        }

    fun getCurrentBookingOutTime(bookingId: Long) = bookingDao.getCurrentBookingOutTime(bookingId)

    fun getCurrentBookingStatus(bookingId: Long) = bookingDao.getCurrentBookingStatus(bookingId)
    fun getCurrentPaymentStatus(paymentId: Long) = paymentDao.getCurrentPaymentStatus(paymentId)
}