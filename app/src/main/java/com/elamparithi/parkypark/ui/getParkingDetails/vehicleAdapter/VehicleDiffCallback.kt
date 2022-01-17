package com.elamparithi.parkypark.ui.getParkingDetails.vehicleAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elamparithi.parkypark.data.local.model.VehicleWithVehicleType

class VehicleDiffCallback : DiffUtil.ItemCallback<VehicleWithVehicleType>() {
    override fun areItemsTheSame(
        oldItem: VehicleWithVehicleType,
        newItem: VehicleWithVehicleType
    ): Boolean {
        return oldItem.vehicleId == newItem.vehicleId
    }

    override fun areContentsTheSame(
        oldItem: VehicleWithVehicleType,
        newItem: VehicleWithVehicleType
    ): Boolean {
        return oldItem == newItem
    }
}