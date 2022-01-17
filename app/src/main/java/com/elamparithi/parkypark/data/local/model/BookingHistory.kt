package com.elamparithi.parkypark.data.local.model
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo


data class BookingHistory(
    @ColumnInfo(name = "booking_id")
    val bookingId: Long,
    @ColumnInfo(name = "parking_type")
    val parkingType: Int,
    @ColumnInfo(name = "booking_status")
    val bookingStatus: Int,
    @ColumnInfo(name = "vehicle_id")
    val vehicleId: Long,
    @ColumnInfo(name = "plate")
    var plateNo: String = "",
    @ColumnInfo(name = "vehicle_type_id")
    val vehicleTypeId: Long,
    @ColumnInfo(name = "icon")
    val vehicleIcon: Int,
    @ColumnInfo(name = "vehicle_type_name")
    var vehicleTypeName: String  = "",
    @ColumnInfo(name = "parking_space_id")
    val parkingSpaceId: Long,
    @ColumnInfo(name = "parking_floor_id")
    val parkingFloorId: Long,
    @ColumnInfo(name = "parking_lot_id")
    val parkingLotId: Long,
    @ColumnInfo(name = "coupon_id")
    val couponId: Long,
    @ColumnInfo(name = "coupon_name")
    val name: String?,
    @ColumnInfo(name = "coupon_code")
    val code: String?,
    @ColumnInfo(name = "in_time")
    val inTime: Long,
    @ColumnInfo(name = "out_time")
    val outTime: Long,
    @ColumnInfo(name = "payment_id")
    val paymentId: Long,
    @ColumnInfo(name = "payment_status")
    val paymentStatus: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bookingId)
        parcel.writeInt(parkingType)
        parcel.writeInt(bookingStatus)
        parcel.writeLong(vehicleId)
        parcel.writeString(plateNo)
        parcel.writeLong(vehicleTypeId)
        parcel.writeInt(vehicleIcon)
        parcel.writeString(vehicleTypeName)
        parcel.writeLong(parkingSpaceId)
        parcel.writeLong(parkingFloorId)
        parcel.writeLong(parkingLotId)
        parcel.writeLong(couponId)
        parcel.writeString(name)
        parcel.writeString(code)
        parcel.writeLong(inTime)
        parcel.writeLong(outTime)
        parcel.writeLong(paymentId)
        parcel.writeInt(paymentStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookingHistory> {
        override fun createFromParcel(parcel: Parcel): BookingHistory {
            return BookingHistory(parcel)
        }

        override fun newArray(size: Int): Array<BookingHistory?> {
            return arrayOfNulls(size)
        }
    }

}