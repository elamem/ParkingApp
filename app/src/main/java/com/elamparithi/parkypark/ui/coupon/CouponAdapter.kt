package com.elamparithi.parkypark.ui.coupon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.entity.Coupon
import com.elamparithi.parkypark.databinding.ListItemCouponBinding

class CouponAdapter(private val onCouponSelected: (coupon: Coupon) -> Unit) :
    ListAdapter<Coupon, CouponAdapter.ViewHolder>(
        CouponDiffCallback()
    ) {

    private var selectedCouponPosition: Int = -1
    private var lastSelectedCouponPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ListItemCouponBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.binding.root.setOnClickListener {
            selectedCouponPosition = holder.adapterPosition
            lastSelectedCouponPosition = if (lastSelectedCouponPosition == -1)
                selectedCouponPosition
            else {
                notifyItemChanged(lastSelectedCouponPosition)
                selectedCouponPosition
            }
            val item = getItem(selectedCouponPosition)
            onCouponSelected(item)
            notifyItemChanged(selectedCouponPosition)

        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        if (position == selectedCouponPosition)
            holder.selectedBg()
        else
            holder.defaultBg()
    }

    class ViewHolder(val binding: ListItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Coupon) {

            binding.tvCouponName.text = item.couponName
            binding.tvDiscountFirstHour.apply {
                text = context.getString(R.string.first_hour_off, item.discountPercentFirstHour)
            }
            binding.tvDiscountFirstHour.apply {
                text = context.getString(
                    R.string.remaining_hour_off,
                    item.discountPercentRemainingHour
                )
            }

        }

        fun selectedBg() {
            binding.root.isChecked = true
        }

        fun defaultBg() {
            binding.root.isChecked = false
        }
    }


}