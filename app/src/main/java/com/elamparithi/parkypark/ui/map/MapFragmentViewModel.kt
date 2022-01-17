package com.elamparithi.parkypark.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.elamparithi.parkypark.data.local.database.entity.Location
import com.elamparithi.parkypark.data.local.database.entity.ParkingLot
import com.elamparithi.parkypark.data.repository.HomeRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel() {
    private val isGoogleMapAvailableToUseMLD: MutableLiveData<Boolean> = MutableLiveData()

    fun getParkingLotLocations(): LiveData<List<ParkingLot>> =
        Transformations.switchMap(isGoogleMapAvailableToUseMLD) { isGoogleMapAvailableToUse ->
            isGoogleMapAvailableToUse?.let {
                if (isGoogleMapAvailableToUse)
                    homeRepository.getParkingLotLocations()
                else
                    null
            }
        }

    fun getUserLocation(): LiveData<Location> =
        Transformations.switchMap(isGoogleMapAvailableToUseMLD) { isGoogleMapAvailableToUse ->
            isGoogleMapAvailableToUse?.let {
                if (isGoogleMapAvailableToUse)
                    homeRepository.getUserLocation()
                else
                    null
            }
        }


    fun isGoogleMapAvailableToUse(isGoogleMapAvailableToUse: Boolean) {
        this.isGoogleMapAvailableToUseMLD.postValue(isGoogleMapAvailableToUse)
    }
}