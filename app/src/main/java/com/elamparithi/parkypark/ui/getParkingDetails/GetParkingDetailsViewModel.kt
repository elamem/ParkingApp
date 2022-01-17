package com.elamparithi.parkypark.ui.getParkingDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.data.local.database.entity.Coupon
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import com.elamparithi.parkypark.data.local.model.VehicleWithVehicleType
import com.elamparithi.parkypark.data.repository.HomeRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import com.elamparithi.parkypark.utils.AuthenticationState
import com.elamparithi.parkypark.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GetParkingDetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val compositeDisposable: CompositeDisposable
) :
    BaseViewModel() {

    var selectedParkingTypeValue = Constants.PARKING_TYPE_PARK_NOW
    private var selectedVehicleId: Long = -1
    private var coupon: Coupon? = null
    var selectedVehicleTypeName = ""
    var selectedVehicleTypePosition = -1
    val selectedVehicleTypeIdMLD: MutableLiveData<Long> = MutableLiveData()
    private val newVehiclePlateNumberMLD: MutableLiveData<String> = MutableLiveData()


    fun getAvailableVehicleTypes(): LiveData<MutableList<VehicleType>> =
        homeRepository.getAvailableVehicleTypes()

    fun getUserVehicleList(): LiveData<List<VehicleWithVehicleType>> {
        return Transformations.switchMap(homeRepository.getCurrentActiveUserId()) { currentUserId ->
            if (currentUserId != null && currentUserId > 0)
                Transformations.switchMap(selectedVehicleTypeIdMLD) { selectedVehicleTypeId ->
                    if (selectedVehicleTypeId > 0)
                        homeRepository.getUserVehicleList(currentUserId, selectedVehicleTypeId)
                    else {
                        val emptyList = emptyList<VehicleWithVehicleType>()
                        val emptyData = MutableLiveData<List<VehicleWithVehicleType>>()
                        emptyData.value = emptyList
                        emptyData
                    }
                }
            else {
                val emptyList = emptyList<VehicleWithVehicleType>()
                val emptyData = MutableLiveData<List<VehicleWithVehicleType>>()
                emptyData.value = emptyList
                emptyData
            }

        }
    }


    fun insertNewVehicle(vehicleNumber: String) {
        compositeDisposable.add(homeRepository.insertNewVehicle(
            selectedVehicleTypeIdMLD.value!!,
            vehicleNumber
        )
            .observeOn(Schedulers.io())
            .subscribe({
                toastStringMLD.postValue(it)
                Timber.e(it)
            }, {
                Timber.e(it.message.toString())
            })
        )
    }

    fun updateNewVehiclePlateDetails(vehicleNumber: String) {
        newVehiclePlateNumberMLD.value = vehicleNumber
    }

    fun updateSelectedVehicleTypeId(currentlySelectedVehicleTypeId: Long) {
        selectedVehicleTypeIdMLD.value = currentlySelectedVehicleTypeId
    }

    fun getUserLoginStatus(): AuthenticationState {
        return if (FirebaseAuth.getInstance().currentUser != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun setSelectedCoupon(coupon: Coupon?) {
        this.coupon = coupon
    }

    fun getBookingDetails(): Booking {
        return Booking(
            selectedVehicleId,
            coupon?.couponId ?: -1,
            -1L,
            -1L,
            0f,
            selectedParkingTypeValue,
            Constants.BOOKING_STATUS_IN_PROGRESS,
            -1L,
            -1L
        )
    }

    fun setSelectedVehicleId(id: Long) {
        this.selectedVehicleId = id
    }

    fun getSelectedVehicleId() = this.selectedVehicleId

    fun getSelectedVehicleTypeId() = this.selectedVehicleTypeIdMLD.value ?: -1

    fun setSelectedParkingType(parkingType: Int) {
        this.selectedParkingTypeValue = parkingType
    }


}