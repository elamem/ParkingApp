package com.elamparithi.parkypark.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.ParkingSpace
import io.reactivex.Single

@Dao
interface ParkingSpaceDao {

    @Insert
    suspend fun insert(parkingSpace: ParkingSpace)

    @Insert
    suspend fun insert(parkingSpace: List<ParkingSpace>)

    @Query("""SELECT MIN(ps.parking_space_id) FROM parking_space as ps 
        LEFT JOIN parking_floor as pf ON ps.parking_floor_id = pf.parking_floor_id 
        LEFT JOIN parking_lot as pl ON pl.parking_lot_id = pf.parking_lot_id 
        WHERE 
        pl.parking_lot_id = :id 
        AND ps.status = :parkingSpaceStatus 
        AND ps.vehicle_type_id = :vehicleTypeId""")
    fun getAvailableParkingSpace(id: Long, vehicleTypeId: Long, parkingSpaceStatus : Int) : Single<Long>

    @Query("UPDATE parking_space SET status = :parkingSpaceStatus WHERE parking_space_id =:selectedParkingSpaceId")
    fun updateParkingSpaceStatus(selectedParkingSpaceId: Long, parkingSpaceStatus: Int)

    @Query("UPDATE parking_space SET status =:status WHERE parking_space_id =:currentParkingSpaceId")
    fun updateGivenParkingSpaceStatus(currentParkingSpaceId: Long, status: Int) : Single<Int>

}
