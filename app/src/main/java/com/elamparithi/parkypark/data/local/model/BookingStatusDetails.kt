package com.elamparithi.parkypark.data.local.model

import androidx.room.ColumnInfo

data class BookingStatusDetails(
    @ColumnInfo(name = "booking_id")
    var bookingId: Long,
    @ColumnInfo(name = "booking_status")
    val status: Int
)
