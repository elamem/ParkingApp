package com.elamparithi.parkypark.ui.findParking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.data.repository.BookingRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FindParkingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val compositeDisposable: CompositeDisposable,
    private val bookingRepository: BookingRepository
) : BaseViewModel() {

    private lateinit var bookingDetails: Booking
    private var isCancelable: Boolean = true
    private var loaderLabel: String = ""
    val bookingIdMLD: MutableLiveData<Long> = MutableLiveData()

    init {
        savedStateHandle.get<String>(FindParkingDialogFragment.KEY_LOADER_LABEL)
            ?.let { loaderLabel ->
                this.loaderLabel = loaderLabel
            }
        savedStateHandle.get<Booking>(FindParkingDialogFragment.KEY_BOOKING_DETAILS)
            ?.let { bookingDetails ->
                this.bookingDetails = bookingDetails
            }
        savedStateHandle.get<Boolean>(FindParkingDialogFragment.KEY_LOADER_IS_CANCELABLE)
            ?.let { isCancelable ->
                this.isCancelable = isCancelable
            }
        findOptimalParkingLot(bookingDetails)
    }

    private fun findOptimalParkingLot(bookingDetails: Booking) {
        val subscribe = bookingRepository.findOptimalParking(bookingDetails)
            .observeOn(Schedulers.io())
            .subscribe({
                bookingIdMLD.postValue(it)
            }, {
                Timber.e(it.message.toString())
            })
        compositeDisposable.add(subscribe)
    }

    fun getLoaderLabel() = loaderLabel
    fun getIsCancelable() = isCancelable

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}