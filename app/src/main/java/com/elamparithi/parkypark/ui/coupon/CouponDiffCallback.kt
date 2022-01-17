package com.elamparithi.parkypark.ui.coupon

import androidx.recyclerview.widget.DiffUtil
import com.elamparithi.parkypark.data.local.database.entity.Coupon

class CouponDiffCallback : DiffUtil.ItemCallback<Coupon>() {
    override fun areItemsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
        return oldItem.couponId == newItem.couponId
    }

    override fun areContentsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
        return oldItem == newItem
    }
}