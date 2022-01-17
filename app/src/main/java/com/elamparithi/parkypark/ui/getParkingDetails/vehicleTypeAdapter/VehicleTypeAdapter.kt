package com.elamparithi.parkypark.ui.getParkingDetails.vehicleTypeAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import com.elamparithi.parkypark.databinding.ListItemVehicleTypeBinding

class VehicleTypeAdapter(private val onVehicleTypeSelected: (id: Long, name: String, position: Int) -> Unit) :
    ListAdapter<VehicleType, VehicleTypeAdapter.ViewHolder>(VehicleTypeDiffCallback()) {

    private var selectedVehicleTypePosition: Int = -1
    private var lastSelectedVehicleTypePosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ListItemVehicleTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.binding.root.setOnClickListener {
            selectedVehicleTypePosition = holder.adapterPosition
            lastSelectedVehicleTypePosition = if (lastSelectedVehicleTypePosition == -1)
                selectedVehicleTypePosition
            else {
                notifyItemChanged(lastSelectedVehicleTypePosition)
                selectedVehicleTypePosition
            }
            val item = getItem(selectedVehicleTypePosition)
            onVehicleTypeSelected(
                item.vehicleTypeId,
                item.vehicleTypeName,
                selectedVehicleTypePosition
            )
            notifyItemChanged(selectedVehicleTypePosition)

        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        if (position == selectedVehicleTypePosition)
            holder.selectedBg()
        else
            holder.defaultBg()
    }

    class ViewHolder(val binding: ListItemVehicleTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VehicleType) {
            binding.ivVehicleTypeIcon.setImageResource(item.icon)
            binding.tvVehicleTypeName.text = item.vehicleTypeName
        }

        fun selectedBg() {
            binding.root.isChecked = true
        }

        fun defaultBg() {
            binding.root.isChecked = false
        }
    }

    fun setSelectedVehicleTypePositionAndLastSelectedPosition(
        selectedVehicleTypePosition: Int,
        lastSelectedVehicleTypePosition: Int
    ) {
        this.selectedVehicleTypePosition = selectedVehicleTypePosition
        this.lastSelectedVehicleTypePosition = lastSelectedVehicleTypePosition
    }
}