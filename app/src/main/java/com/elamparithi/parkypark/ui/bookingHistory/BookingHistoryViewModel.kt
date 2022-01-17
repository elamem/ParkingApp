package com.elamparithi.parkypark.ui.bookingHistory

import androidx.lifecycle.*
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import com.elamparithi.parkypark.data.model.VehicleTypeRange
import com.elamparithi.parkypark.data.repository.BookingRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingHistoryViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
) : BaseViewModel() {

    var getParkingDetailsBottomSheetCurrentState: Int = 0
    private val vehicleTypeRangeMLD: MutableLiveData<VehicleTypeRange> = MutableLiveData()
    val vehicleTypeListLD: LiveData<MutableList<VehicleType>> =
        bookingRepository.getAvailableVehicleTypes()
    private val vehicleTypeObserver = Observer<List<VehicleType>> { vehicleTypeList ->
        vehicleTypeList?.let {
            vehicleTypeRangeMLD.apply {
                value = VehicleTypeRange(it[0].vehicleTypeId, it[it.size - 1].vehicleTypeId)
            }
        } ?: run {
            vehicleTypeRangeMLD.apply {
                value = VehicleTypeRange(0, 0)
            }
        }
    }

    init {
        vehicleTypeListLD.observeForever(vehicleTypeObserver)
    }

    fun getAvailableBookingList() =
        Transformations.switchMap(bookingRepository.getCurrentActiveUserId()) userId@{ currentUserId ->
            return@userId currentUserId?.let { userId ->
                Transformations.switchMap(vehicleTypeRangeMLD) { vehicleTypeRange ->
                    return@switchMap bookingRepository.getAvailableBookingList(
                        userId,
                        vehicleTypeRange.typeFrom,
                        vehicleTypeRange.typeTo
                    )
                }
            }
        }

    override fun onCleared() {
        super.onCleared()
        vehicleTypeListLD.removeObserver(vehicleTypeObserver)
    }

    fun updateSelectedVehicleTypeRange(vehicleTypeFrom: Long, vehicleTypeTo: Long) {
        vehicleTypeRangeMLD.apply {
            value = VehicleTypeRange(vehicleTypeFrom, vehicleTypeTo)
        }
    }
}