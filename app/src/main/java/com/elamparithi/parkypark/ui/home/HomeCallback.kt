package com.elamparithi.parkypark.ui.home

import com.elamparithi.parkypark.data.local.database.entity.Booking

interface HomeCallback {
    fun handleParkingDetailsBottomSheet(bottomSheetState: Int)
    fun callUserLogin()
    fun getParkingDetailsBottomSheetCurrentState(): Int
    fun checkLocationAccess(booking: Booking);
    fun getParkingDetailsBackPressed();
}