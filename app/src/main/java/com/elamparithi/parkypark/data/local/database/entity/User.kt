package com.elamparithi.parkypark.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @Embedded
    val location: Location,
    @ColumnInfo(name = "isActive")
    val isActive: Boolean
) {
    @ColumnInfo(name = "user_id")
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0
}