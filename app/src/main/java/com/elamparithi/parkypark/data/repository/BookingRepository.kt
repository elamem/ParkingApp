package com.elamparithi.parkypark.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.data.local.database.entity.Payment
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import com.elamparithi.parkypark.data.local.model.BookingHistory
import com.elamparithi.parkypark.data.local.model.Compare
import com.elamparithi.parkypark.data.remote.NetworkService
import com.elamparithi.parkypark.utils.Common
import com.elamparithi.parkypark.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val userDao: UserDao,
    private val parkingLotDao: ParkingLotDao,
    private val bookingDao: BookingDao,
    private val paymentDao: PaymentDao,
    private val parkingSpaceDao: ParkingSpaceDao,
    private val vehicleDao: VehicleDao,
    private val vehicleTypeDao: VehicleTypeDao,
    private val couponUserRelDao: CouponUserRelDao,
    private val networkService: NetworkService,
    private val application: Application
) {

    /**
     * This method helps to find the optimal parking lot for the given vehicle
     * Step 1 - gets all the available parking lots ids and its locations based on the vehicle type. If no space available for the vehicle type then
     * that parking lot will be rejected.
     * Step 2 - Gets the distance between parking lot and the user using google directions api
     * Step 3 - Based on the distance nearest parking lot will be allotted to the user.
     */
    fun findOptimalParking(bookingDetails: Booking): Single<Long> {
        return vehicleDao.getVehicleTypeId(bookingDetails.vehicleId)
            .flatMap { selectedVehicleTypeId ->
                Single.zip(
                    userDao.getUserLocationObservable(),
                    parkingLotDao.getParkingLotIdAndLocation(
                        selectedVehicleTypeId,
                        Constants.PARKING_SPACE_STATUS_FREE
                    ),
                    { userLocation, parkingLotsWithLocation ->
                        parkingLotsWithLocation.forEach {
                            it.userLocation = userLocation
                        }
                        parkingLotsWithLocation
                    }
                )
            }.map { parkingLotWithLocationList ->

                parkingLotWithLocationList.forEach { parkingLotWithLocation ->
                    val latLangOrigin =
                        "${parkingLotWithLocation.userLocation.latitude},${parkingLotWithLocation.userLocation.longitude}"
                    val latLangDestination =
                        "${parkingLotWithLocation.parkingLotLocation.latitude},${parkingLotWithLocation.parkingLotLocation.longitude}"
                    val distanceApiCall =
                        networkService.getDirection(latLangOrigin, latLangDestination).map {
                            parkingLotWithLocation.distance =
                                it.routes[0].legsList[0].distance.text.split(" ")[0].toFloat()
                            Timber.e(
                                "Parking lot ${parkingLotWithLocation.parkingLotId} distance ${parkingLotWithLocation.distance}"
                            )
                        }.subscribe({

                        }, {
                            Timber.e(it.message.toString())
                        })
                }
                parkingLotWithLocationList
            }.flatMap { parkingLotWithDistanceList ->
                val nearestParkingLot = parkingLotWithDistanceList.reduce(Compare::min)
                Timber.e("nearestParkingLot $nearestParkingLot")
                vehicleDao.getVehicleTypeId(bookingDetails.vehicleId).flatMap { vehicleTypeId ->
                    parkingSpaceDao.getAvailableParkingSpace(
                        nearestParkingLot.parkingLotId,
                        vehicleTypeId,
                        Constants.PARKING_SPACE_STATUS_FREE
                    ).flatMap { selectedParkingSpaceId ->
                        bookingDetails.parkingSpaceId = selectedParkingSpaceId
                        bookingDetails.distance = nearestParkingLot.distance
                        Timber.e("allotted parking space $selectedParkingSpaceId")
                        parkingSpaceDao.updateParkingSpaceStatus(
                            selectedParkingSpaceId,
                            Constants.PARKING_SPACE_STATUS_IN_PROGRESS
                        )
                        paymentDao.insert(Payment(0F, 0F,0F,0F,0F,0F,Constants.PAYMENT_STATUS_BOOKING_CREATED))
                    }
                }
            }.flatMap { paymentId ->
                bookingDetails.paymentId = paymentId
                bookingDao.insert(bookingDetails)
            }.map { bookingId->
                bookingId
            }
            .subscribeOn(Schedulers.io())
    }

    fun getCurrentlyActiveBookingFullDetails() = bookingDao.getCurrentlyActiveBookingFullDetails(
        Constants.BOOKING_STATUS_IN_PROGRESS, Constants.BOOKING_STATUS_CONFIRMED
    )

    fun confirmTheGivenBooking(
        currentBookingId: Long,
        currentBookingPaymentId: Long,
        currentParkingSpaceId: Long,
        currentCouponId: Long
    ): Single<List<Int>> {
        return if (currentCouponId == -1L)
            Single.merge(
                bookingDao.updateGivenBookingStatus(currentBookingId, Constants.BOOKING_STATUS_CONFIRMED)
                    .subscribeOn(Schedulers.io()),
                parkingSpaceDao.updateGivenParkingSpaceStatus(currentParkingSpaceId, Constants.PARKING_SPACE_STATUS_OCCUPIED)
                    .subscribeOn(Schedulers.io()),
                paymentDao.updateGivenBookingPaymentStatus(
                    currentBookingPaymentId,
                    Constants.PAYMENT_STATUS_IN_DUE
                )
                    .subscribeOn(Schedulers.io()),
            ).toList()
        else
            Single.merge(
                bookingDao.updateGivenBookingStatus(
                    currentBookingId,
                    Constants.BOOKING_STATUS_CONFIRMED
                )
                    .subscribeOn(Schedulers.io()),
                parkingSpaceDao.updateGivenParkingSpaceStatus(
                    currentParkingSpaceId,
                    Constants.PARKING_SPACE_STATUS_OCCUPIED
                )
                    .subscribeOn(Schedulers.io()),
                paymentDao.updateGivenBookingPaymentStatus(
                    currentBookingPaymentId,
                    Constants.PAYMENT_STATUS_IN_DUE
                )
                    .subscribeOn(Schedulers.io()),
                couponUserRelDao.updateGivenCouponUserStatus(currentCouponId, Constants.COUPON_STATUS_USED)
                    .subscribeOn(Schedulers.io())
            ).toList()
    }

    fun cancelTheGivenBooking(
        currentBookingId: Long,
        currentBookingPaymentId: Long,
        currentParkingSpaceId: Long,
        currentCouponId: Long
    ): Single<String> {
        return if (currentCouponId == -1L)
            Single.merge(
                bookingDao.updateGivenBookingStatus(currentBookingId, Constants.BOOKING_STATUS_CANCELED)
                    .subscribeOn(Schedulers.io()),
                parkingSpaceDao.updateGivenParkingSpaceStatus(currentParkingSpaceId, Constants.PARKING_SPACE_STATUS_FREE)
                    .subscribeOn(Schedulers.io()),
                paymentDao.updateGivenBookingPaymentStatus(
                    currentBookingPaymentId,
                    Constants.PAYMENT_STATUS_BOOKING_CANCELED
                )
                    .subscribeOn(Schedulers.io()),
            ).toList().map { updateList->
                if(updateList.size == 3) {
                    application.getString(R.string.successfully_canceled)
                } else {
                    application.getString(R.string.cancellation_failed)
                }
            }
        else
            Single.merge(
                bookingDao.updateGivenBookingStatus(
                    currentBookingId,
                    Constants.BOOKING_STATUS_CANCELED
                )
                    .subscribeOn(Schedulers.io()),
                parkingSpaceDao.updateGivenParkingSpaceStatus(
                    currentParkingSpaceId,
                    Constants.PARKING_SPACE_STATUS_FREE
                )
                    .subscribeOn(Schedulers.io()),
                paymentDao.updateGivenBookingPaymentStatus(
                    currentBookingPaymentId,
                    Constants.PAYMENT_STATUS_BOOKING_CANCELED
                )
                    .subscribeOn(Schedulers.io()),
                couponUserRelDao.updateGivenCouponUserStatus(currentCouponId, Constants.COUPON_STATUS_ACTIVE)
                    .subscribeOn(Schedulers.io())
            ).toList().map {updateList->
                if(updateList.size == 4) {
                    application.getString(R.string.successfully_canceled)
                } else {
                    application.getString(R.string.cancellation_failed)
                }
            }
    }

    fun activateTheGivenBooking(
        currentBookingId: Long,
        currentParkingSpaceId: Long
    ): Single<String> {
         return Single.merge(
                bookingDao.activateBookingStatus(currentBookingId, Constants.BOOKING_STATUS_ACTIVE, Common.getCurrentTimeStamp())
                    .subscribeOn(Schedulers.io()),
                parkingSpaceDao.updateGivenParkingSpaceStatus(currentParkingSpaceId, Constants.PARKING_SPACE_STATUS_OCCUPIED)
                    .subscribeOn(Schedulers.io())
            ).toList().map { updateList->
             if(updateList.size == 2) {
                 application.getString(R.string.parked_successfully)
             } else {
                 application.getString(R.string.parking_failed)
             }
         }
    }


    fun getCurrentActiveUserId(): LiveData<Long> {
        return  userDao.getCurrentActiveUserId()
    }

    fun getAvailableVehicleTypes(): LiveData<MutableList<VehicleType>> =
        vehicleTypeDao.getAvailableVehicleTypes()

    fun getAvailableBookingList(userId : Long , vehicleTypeFrom :Long ,vehicleTypeTo :Long) = bookingDao.getAvailableBookingList(userId, vehicleTypeFrom, vehicleTypeTo)
}