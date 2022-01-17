package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.ParkingLot
import com.elamparithi.parkypark.data.local.model.ParkingLotWithUserLocation
import io.reactivex.Single

@Dao
interface ParkingLotDao {

    @Insert
    suspend fun insert(parkingLot: ParkingLot)

    @Insert
    suspend fun insert(parkingLot: List<ParkingLot>): List<Long>

    @Query("SELECT * FROM parking_lot")
    fun getAllParkingLots(): Single<List<ParkingLot>>

    @Query("SELECT * FROM parking_lot")
    fun getParkingLotLocations(): LiveData<List<ParkingLot>>

    @Query("SELECT pl.parking_lot_id, pl.latitude, pl.longitude FROM parking_space as ps LEFT JOIN parking_floor as pf on ps.parking_floor_id = pf.parking_floor_id LEFT JOIN parking_lot as pl ON pl.parking_lot_id = pf.parking_lot_id WHERE ps.vehicle_type_id = :vehicleTypeId AND ps.status = :parkingSpaceStatus GROUP BY pl.parking_lot_id")
    fun getParkingLotIdAndLocation(vehicleTypeId: Long, parkingSpaceStatus : Int): Single<List<ParkingLotWithUserLocation>>

}