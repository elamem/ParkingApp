package com.elamparithi.parkypark.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.elamparithi.parkypark.data.local.database.entity.ParkingFloor

@Dao
interface ParkingFloorDao {

    @Insert
    suspend fun insert(parkingFloor: ParkingFloor)

    @Insert
    suspend fun insert(parkingFloor: List<ParkingFloor>) : List<Long>

}