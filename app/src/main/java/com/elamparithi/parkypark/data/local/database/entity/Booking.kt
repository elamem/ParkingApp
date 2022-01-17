package com.elamparithi.parkypark.data.local.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(
    tableName = "booking",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = arrayOf("vehicle_id"),
            childColumns = arrayOf("vehicle_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParkingSpace::class,
            parentColumns = arrayOf("parking_space_id"),
            childColumns = arrayOf("parking_space_id"),
            onDelete = ForeignKey.CASCADE
        ),
                ForeignKey(
                entity = Payment::class,
        parentColumns = arrayOf("payment_id"),
        childColumns = arrayOf("payment_id"),
        onDelete = ForeignKey.CASCADE
)
    ]
)
data class Booking(
    @ColumnInfo(name = "vehicle_id", index = true)
    val vehicleId: Long,
    @ColumnInfo(name = "coupon_id")
    val couponId: Long,
    @ColumnInfo(name = "parking_space_id", index = true)
    var parkingSpaceId: Long,
    @ColumnInfo(name = "payment_id", index = true)
    var paymentId: Long,
    @ColumnInfo(name = "distance")
    var distance: Float,
    @ColumnInfo(name = "parking_type")
    val parkingType: Int,
    @ColumnInfo(name = "booking_status")
    val status: Int,
    @ColumnInfo(name = "in_time")
    val inTime: Long,
    @ColumnInfo(name = "out_time")
    val outTime: Long
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "booking_id")
    var bookingId: Long = 0

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong()
    ) {
        bookingId = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(vehicleId)
        parcel.writeLong(couponId)
        parcel.writeLong(parkingSpaceId)
        parcel.writeLong(paymentId)
        parcel.writeFloat(distance)
        parcel.writeInt(parkingType)
        parcel.writeInt(status)
        parcel.writeLong(inTime)
        parcel.writeLong(outTime)
        parcel.writeLong(bookingId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }


}