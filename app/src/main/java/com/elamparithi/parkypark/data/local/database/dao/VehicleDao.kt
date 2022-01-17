package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.Vehicle
import com.elamparithi.parkypark.data.local.model.VehicleWithVehicleType
import io.reactivex.Single

@Dao
interface VehicleDao {

    @Insert
    fun insert(vehicle: Vehicle): Single<Long>


    @Query(
        """SELECT vh.* , vt.vehicle_type_name, vt.icon, vt.price_first_hour, vt.price_remaining_hour  FROM 
        vehicle as vh LEFT JOIN vehicle_type  as vt on vh.vehicle_type_id = vt.vehicle_type_id
WHERE user_id =:userId AND vh.vehicle_type_id =:vehicleTypeId AND (SELECT COUNT(*) FROM booking WHERE booking.vehicle_id = vh.vehicle_id AND booking_status = :bookingStatus) = 0"""
    )
    fun getUserVehicleList(
        userId: Long,
        vehicleTypeId: Long,
        bookingStatus: Int
    ): LiveData<List<VehicleWithVehicleType>>

    @Query("SELECT vehicle_type_id FROM vehicle WHERE vehicle_id =:vehicleId")
    fun getVehicleTypeId(vehicleId: Long): Single<Long>

    @Query("SELECT COUNT(vehicle_type_id) FROM vehicle WHERE user_id =:userId AND plate =:vehicleNumber COLLATE NOCASE")
    fun verifyVehicleAlreadyRegistered(vehicleNumber: String, userId: Long): Single<Int>
}
