package com.elamparithi.parkypark.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elamparithi.parkypark.data.local.database.entity.Location
import com.elamparithi.parkypark.data.local.database.entity.User
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User): Long

    @Insert
    suspend fun insert(user: List<User>)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Single<List<User>>

    @Query("DELETE FROM user")
    suspend fun clearUserDetails()

    @Query("SELECT user_id FROM user WHERE isActive = 1")
    fun getCurrentActiveUserId(): LiveData<Long>

    @Query("SELECT user_id FROM user WHERE isActive = 1")
    fun getCurrentActiveUserIdSingle(): Single<Long>

    @Query("SELECT latitude, longitude FROM user WHERE isActive = 1")
    fun getUserLocationObservable(): Single<Location>

    @Query("SELECT latitude, longitude FROM user WHERE isActive = 1")
    fun getUserLocation(): LiveData<Location>
}