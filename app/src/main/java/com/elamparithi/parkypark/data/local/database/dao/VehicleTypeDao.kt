package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import io.reactivex.Single

@Dao
interface VehicleTypeDao {
    @Insert
    suspend fun insert(vehicleType: VehicleType)

    @Insert
    suspend fun insert(vehicleType: List<VehicleType>): List<Long>

    @Query("SELECT * FROM vehicle_type")
    fun getAvailableVehicleTypes(): LiveData<MutableList<VehicleType>>

    @Query(
        """SELECT vt.* FROM vehicle_type as vt LEFT JOIN vehicle as vh
        ON vt.vehicle_type_id = vh.vehicle_type_id
        WHERE vh.vehicle_id = :vehicleId
    """
    )
    fun getGivenVehicleTypeDetails(vehicleId: Long): Single<VehicleType>


//    @Query("SELECT * FROM vehicle WHERE user_id=:userId AND vehicle_type_id =:vehicleTypeId")
//    fun getUserVehicleWithTypeList(userId: Long, vehicleTypeId: Long): LiveData<List<VehicleWithVehicleType>>

}