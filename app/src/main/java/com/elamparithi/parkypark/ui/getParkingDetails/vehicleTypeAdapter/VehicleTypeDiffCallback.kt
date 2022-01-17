package com.elamparithi.parkypark.ui.getParkingDetails.vehicleTypeAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elamparithi.parkypark.data.local.database.entity.VehicleType

class VehicleTypeDiffCallback : DiffUtil.ItemCallback<VehicleType>() {
    override fun areItemsTheSame(oldItem: VehicleType, newItem: VehicleType): Boolean {
        return oldItem.vehicleTypeId == newItem.vehicleTypeId
    }

    override fun areContentsTheSame(oldItem: VehicleType, newItem: VehicleType): Boolean {
        return oldItem == newItem
    }
}