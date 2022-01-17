package com.elamparithi.parkypark.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.elamparithi.parkypark.R
import java.text.SimpleDateFormat
import java.util.*

object Common {

    fun getDate(timeStamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            val netDate = Date(timeStamp)
            sdf.format(netDate)
        } catch (e: Exception) {
            ""
        }
    }

    fun getDateAndTime(timeStamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.ENGLISH)
            val netDate = Date(timeStamp * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            ""
        }
    }

    fun getCurrentTimeStamp() = System.currentTimeMillis() / 1000

    fun getParkingStatusLabel(bookingStatus: Int, context: Context): String {
        return when (bookingStatus) {
            Constants.BOOKING_STATUS_ACTIVE -> context.getString(R.string.active)
            Constants.BOOKING_STATUS_CONFIRMED -> context.getString(R.string.confirmed)
            Constants.BOOKING_STATUS_COMPLETED -> context.getString(R.string.completed)
            Constants.BOOKING_STATUS_CANCELED -> context.getString(R.string.canceled)
            else -> ""
        }
    }

    fun getPaymentStatusLabel(paymentStatus: Int, context: Context): String {
        return when (paymentStatus) {
            Constants.PAYMENT_STATUS_IN_DUE -> context.getString(R.string.pending)
            Constants.PAYMENT_STATUS_PAID -> context.getString(R.string.paid)
            Constants.PAYMENT_STATUS_BOOKING_CANCELED -> context.getString(R.string.canceled)
            else -> ""
        }
    }

    fun getTotalHours(inTime: Long, outTime: Long): Int {
        val diff = outTime - inTime
        return if (diff == 0L) 0
        else if (diff < 3600) 1
        else if (diff > 3600 && diff % 3600 == 0L)
            (diff / 3600).toInt()
        else
            (diff / 3600).toInt() + 1
    }

    fun calculateDiscountPrice(total: Float, discountPercentage: Int): Float =
        total * discountPercentage / 100F


    fun hideKeyboard(view: View, context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }
}