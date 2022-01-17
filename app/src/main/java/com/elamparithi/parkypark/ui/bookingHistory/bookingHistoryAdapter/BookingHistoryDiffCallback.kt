package com.elamparithi.parkypark.ui.bookingHistory.bookingHistoryAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elamparithi.parkypark.data.local.model.BookingHistory

class BookingHistoryDiffCallback : DiffUtil.ItemCallback<BookingHistory>() {
    override fun areItemsTheSame(oldItem: BookingHistory, newItem: BookingHistory): Boolean {
        return oldItem.couponId == newItem.couponId
    }

    override fun areContentsTheSame(oldItem: BookingHistory, newItem: BookingHistory): Boolean {
        return oldItem == newItem
    }
}