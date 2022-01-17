package com.elamparithi.parkypark.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.local.database.entity.ParkingLot
import com.elamparithi.parkypark.data.local.database.entity.Vehicle
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import com.elamparithi.parkypark.data.local.model.VehicleWithVehicleType
import com.elamparithi.parkypark.utils.Constants
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val vehicleTypeDao: VehicleTypeDao,
    private val vehicleDao: VehicleDao,
    private val userDao: UserDao,
    private val bookingDao: BookingDao,
    private val parkingLotDao: ParkingLotDao,
    private val application: Application
) {

    fun getAvailableVehicleTypes(): LiveData<MutableList<VehicleType>> =
        vehicleTypeDao.getAvailableVehicleTypes()

    fun getUserVehicleList(
        userId: Long,
        vehicleTypeId: Long
    ): LiveData<List<VehicleWithVehicleType>> =
        vehicleDao.getUserVehicleList(userId, vehicleTypeId, Constants.BOOKING_STATUS_ACTIVE)

    fun insertNewVehicle(selectedVehicleTypeId: Long, vehicleNumber: String) =
        userDao.getCurrentActiveUserIdSingle().flatMap { userId ->
            vehicleDao.verifyVehicleAlreadyRegistered(vehicleNumber, userId)
                .flatMap { vehicleCount ->
                    if (vehicleCount == 0) {
                        vehicleDao.insert(Vehicle(selectedVehicleTypeId, userId, vehicleNumber))
                    } else {
                        Single.just(-1)
                    }
                }
        }.map {
            if (it == -1L) {
                application.getString(R.string.vehicleAlreadyRegistered)
            } else {
                application.getString(R.string.vehicleRegisteredSuccessfully)
            }
        }
            .subscribeOn(Schedulers.io())

    fun getCurrentActiveUserId(): LiveData<Long> {
        return userDao.getCurrentActiveUserId()
    }

    fun getParkingLotLocations(): LiveData<List<ParkingLot>> =
        parkingLotDao.getParkingLotLocations()

    fun getUserLocation() = userDao.getUserLocation()

    fun getCurrentlyActiveOrInProgressBookingDetails() =
        bookingDao.getCurrentlyActiveOrInProgressBookingDetails(
            Constants.PARKING_TYPE_PARK_NOW,
            Constants.BOOKING_STATUS_IN_PROGRESS,
            Constants.BOOKING_STATUS_CONFIRMED
        )
}