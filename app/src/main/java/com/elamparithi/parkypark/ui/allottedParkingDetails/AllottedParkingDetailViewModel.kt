package com.elamparithi.parkypark.ui.allottedParkingDetails

import androidx.lifecycle.Transformations
import com.elamparithi.parkypark.data.repository.BookingRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AllottedParkingDetailViewModel @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val bookingRepository: BookingRepository
) : BaseViewModel() {

    private var currentBookingId: Long = -1
    private var currentBookingPaymentId: Long = -1
    private var currentParkingSpaceId: Long = -1
    private var currentCouponId: Long = -1

    fun getCurrentlyActiveBookingFullDetails() =
        Transformations.map(bookingRepository.getCurrentlyActiveBookingFullDetails()) { bookingDetails ->
            bookingDetails?.let {
                currentBookingId = bookingDetails.bookingId
                currentBookingPaymentId = bookingDetails.paymentId
                currentParkingSpaceId = bookingDetails.parkingSpaceId
                currentCouponId = bookingDetails.couponId
            }
            bookingDetails
        }

    fun confirmTheGivenBooking() {
        val confirmTheGivenBooking = bookingRepository.confirmTheGivenBooking(
            currentBookingId,
            currentBookingPaymentId,
            currentParkingSpaceId,
            currentCouponId
        )
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.e("updated rows count ${it.size}")
            }, {
                Timber.e(it.message.toString())
            })
        compositeDisposable.add(confirmTheGivenBooking)
    }

    fun cancelTheGivenBooking() {
        val cancelTheGivenBooking = bookingRepository.cancelTheGivenBooking(
            currentBookingId,
            currentBookingPaymentId,
            currentParkingSpaceId,
            currentCouponId
        )
            .observeOn(Schedulers.io())
            .subscribe({
                toastStringMLD.postValue(it)
            }, {
                Timber.e(it.message.toString())
            })
        compositeDisposable.add(cancelTheGivenBooking)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun activateTheGivenBooking() {
        val activateTheGivenBooking =
            bookingRepository.activateTheGivenBooking(currentBookingId, currentParkingSpaceId)
                .observeOn(Schedulers.io())
                .subscribe({
                    toastStringMLD.postValue(it)
                }, {
                    Timber.e(it.message.toString())
                })
        compositeDisposable.add(activateTheGivenBooking)
    }


}