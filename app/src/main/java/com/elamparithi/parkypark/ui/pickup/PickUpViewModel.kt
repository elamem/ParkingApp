package com.elamparithi.parkypark.ui.pickup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.elamparithi.parkypark.data.local.database.entity.Payment
import com.elamparithi.parkypark.data.local.model.BookingHistory
import com.elamparithi.parkypark.data.repository.PickUpRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PickUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val compositeDisposable: CompositeDisposable,
    private val pickUpRepository: PickUpRepository
) : BaseViewModel() {

    private var shouldCalculatePayment: Boolean = false
    lateinit var bookingDetail: BookingHistory
    val paymentDetailMLD: MutableLiveData<Payment> = MutableLiveData()

    init {
        savedStateHandle.get<BookingHistory>(PickUpFragment.KEY_BOOKING_DETAIL)
            ?.let { bookingDetail ->
                this.bookingDetail = bookingDetail
            }

        savedStateHandle.get<Boolean>(PickUpFragment.KEY_CALCULATE_PAYMENT)
            ?.let { shouldCalculatePayment ->
                this.shouldCalculatePayment = shouldCalculatePayment
            }

        if (shouldCalculatePayment) {
            calculatePayment()
        } else {
            getPaymentDetail()
        }
    }

    private fun getPaymentDetail() {
        compositeDisposable.add(
            pickUpRepository.getGivenPaymentIdInfo(bookingDetail.paymentId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ payment ->
                    paymentDetailMLD.postValue(payment)
                }, {
                    Timber.e(it.message.toString())
                })
        )
    }

    private fun calculatePayment() {
        compositeDisposable.add(
            pickUpRepository.calculatePayment(bookingDetail)
                .observeOn(Schedulers.io())
                .subscribe({ payment ->
                    paymentDetailMLD.postValue(payment)
                }, {
                    Timber.e(it.message.toString())
                })
        )
    }

    fun getCurrentBookingOutTime() =
        Transformations.distinctUntilChanged(pickUpRepository.getCurrentBookingOutTime(bookingDetail.bookingId))

    fun getCurrentBookingStatus() =
        Transformations.distinctUntilChanged(pickUpRepository.getCurrentBookingStatus(bookingDetail.bookingId))

    fun getCurrentPaymentStatus() =
        Transformations.distinctUntilChanged(pickUpRepository.getCurrentPaymentStatus(bookingDetail.paymentId))

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun releaseTheVehicle() {
        compositeDisposable.add(
            pickUpRepository.releaseTheVehicle(bookingDetail).observeOn(
                Schedulers.io()
            ).subscribe({
                Timber.i("successfully released the vehicle")
            }, {
                Timber.i(it.message.toString())
            })
        )
    }
}