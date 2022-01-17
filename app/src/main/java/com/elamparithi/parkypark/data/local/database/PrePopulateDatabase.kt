package com.elamparithi.parkypark.data.local.database

import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.dao.*
import com.elamparithi.parkypark.data.local.database.entity.*
import com.elamparithi.parkypark.utils.Constants
import com.google.android.gms.maps.model.LatLng
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class PrePopulateDatabase @Inject constructor(
    private val parkingLotProvider: Provider<ParkingLotDao>,
    private val parkingFloorProvider: Provider<ParkingFloorDao>,
    private val parkingSpaceProvider: Provider<ParkingSpaceDao>,
    private val vehicleTypeProvider: Provider<VehicleTypeDao>,
    private val couponProvider: Provider<CouponDao>,
    private val coroutineScope: CoroutineScope
) {

    fun insertParkingData() {
        coroutineScope.launch {

            val parkingLotList = mutableListOf(
                ParkingLot(
                    "parking 1",
                    Location(13.077361564389617, 80.27208655696298),
                    Constants.TOTAL_FLOOR_PER_PARKING_LOT,
                    Constants.TOTAL_PARKING_SPACE_PER_FLOOR
                ),
                ParkingLot(
                    "parking 2",
                    Location(13.081283137203542, 80.26987424069573),
                    Constants.TOTAL_FLOOR_PER_PARKING_LOT,
                    Constants.TOTAL_PARKING_SPACE_PER_FLOOR
                ),
                ParkingLot(
                    "parking 3",
                    Location(13.078079876946113, 80.27617236628535),
                    Constants.TOTAL_FLOOR_PER_PARKING_LOT,
                    Constants.TOTAL_PARKING_SPACE_PER_FLOOR
                ),
                ParkingLot(
                    "parking 4",
                    Location(13.078118704592287, 80.27758745146531),
                    Constants.TOTAL_FLOOR_PER_PARKING_LOT,
                    Constants.TOTAL_PARKING_SPACE_PER_FLOOR
                )
            )
            val listOfParkingLotIds = parkingLotProvider.get().insert(parkingLotList)

            val parkingFloorList = mutableListOf<ParkingFloor>()
            listOfParkingLotIds.forEach {
                for(i in 1..Constants.TOTAL_FLOOR_PER_PARKING_LOT) {
                    parkingFloorList.add(ParkingFloor(it, Constants.TOTAL_PARKING_SPACE_PER_FLOOR))
                }
            }
            val listOfParkingFloorIds = parkingFloorProvider.get().insert(parkingFloorList)
            val vehicleTypeList = mutableListOf(
                VehicleType("Bike", R.drawable.ic_bike, 30f, 15f),
                VehicleType("Car", R.drawable.ic_car, 50f, 25f),
                VehicleType("Bus", R.drawable.ic_bus, 80f, 40f),
            )
            val listOfVehicleTypeIds = vehicleTypeProvider.get().insert(vehicleTypeList)

            val parkingSpaceList = mutableListOf<ParkingSpace>()

            listOfParkingFloorIds.forEach { floorId ->
                val bikeParking = Constants.TOTAL_PARKING_SPACE_PER_FLOOR * 40 / 100
                val carParking = Constants.TOTAL_PARKING_SPACE_PER_FLOOR * 40 / 100
                val busParking = Constants.TOTAL_PARKING_SPACE_PER_FLOOR * 20 / 100
                for (i in 1..bikeParking) {
                    parkingSpaceList.add(
                        ParkingSpace(
                            floorId,
                            listOfVehicleTypeIds[0],
                            Constants.PARKING_SPACE_STATUS_FREE
                        )
                    )
                }
                for (i in 1..carParking) {
                    parkingSpaceList.add(
                        ParkingSpace(
                            floorId,
                            listOfVehicleTypeIds[1],
                            Constants.PARKING_SPACE_STATUS_FREE
                        )
                    )
                }
                for (i in 1..busParking) {
                    parkingSpaceList.add(
                        ParkingSpace(
                            floorId,
                            listOfVehicleTypeIds[2],
                            Constants.PARKING_SPACE_STATUS_FREE
                        )
                    )
                }
            }
            parkingSpaceProvider.get().insert(parkingSpaceList)

            val couponList = mutableListOf(
                Coupon("First Time", "FIRST",50, 10)
            )
            couponProvider.get().insert(couponList)
        }
    }
}