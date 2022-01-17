package com.elamparithi.parkypark.ui.getParkingDetails.vehicleAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.model.VehicleWithVehicleType
import com.elamparithi.parkypark.databinding.ListItemVehicleDetailBinding

class VehicleAdapter(private val onVehicleSelected: (vehicle: VehicleWithVehicleType) -> Unit) :
    ListAdapter<VehicleWithVehicleType, VehicleAdapter.ViewHolder>(VehicleDiffCallback()) {

    private var selectedVehicleTypePosition: Int = -1
    private var lastSelectedVehicleTypePosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ListItemVehicleDetailBinding.inflate(
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
            onVehicleSelected(item)
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

    class ViewHolder(val binding: ListItemVehicleDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VehicleWithVehicleType) {
            binding.ivVehicleTypeIcon.setImageResource(item.icon)
            binding.tvVehicleTypeName.text = item.typeName
            binding.tvVehiclePlateNo.text = item.plate
            binding.tvVehicleSerialNo.text =
                binding.root.context.getString(R.string.vehicle_label, item.vehicleId)
        }

        fun selectedBg() {
            binding.root.isChecked = true
        }

        fun defaultBg() {
            binding.root.isChecked = false
        }
    }


}