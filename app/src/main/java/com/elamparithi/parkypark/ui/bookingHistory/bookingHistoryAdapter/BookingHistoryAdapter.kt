package com.elamparithi.parkypark.ui.bookingHistory.bookingHistoryAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.model.BookingHistory
import com.elamparithi.parkypark.databinding.ListItemBookingHistoryV2Binding
import com.elamparithi.parkypark.utils.Common
import com.elamparithi.parkypark.utils.Constants

class BookingHistoryAdapter(private val onBookingSelected: (bookingDetail: BookingHistory) -> Unit) :
    ListAdapter<BookingHistory, BookingHistoryAdapter.ViewHolder>(
        BookingHistoryDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ListItemBookingHistoryV2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.binding.btnPickUp.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            onBookingSelected(item)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(val binding: ListItemBookingHistoryV2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookingHistory) {
            binding.layoutVehicleType.tvVehicleTypeName.text = item.vehicleTypeName
            binding.layoutVehicleType.ivVehicleTypeIcon.setImageResource(item.vehicleIcon)
            binding.tvVehiclePlateNo.text = item.plateNo
            binding.tvParkingStatus.text = Common.getParkingStatusLabel(
                item.bookingStatus,
                binding.tvParkingStatus.context.applicationContext
            )
            binding.tvPaymentStatus.text = binding.tvPaymentStatus.context.getString(
                R.string.payment_status, Common.getPaymentStatusLabel(
                    item.paymentStatus,
                    binding.tvPaymentStatus.context.applicationContext
                )
            )
            if (item.bookingStatus == Constants.BOOKING_STATUS_ACTIVE) {
                binding.tvParkingStatus.setTextColor(binding.tvParkingStatus.context.getColor(R.color.green))
                binding.btnPickUp.text =
                    binding.tvPaymentStatus.context.getString(R.string.pay_pick_up)
            } else {
                binding.btnPickUp.text = binding.tvPaymentStatus.context.getString(R.string.summary)
                binding.tvParkingStatus.setTextColor(binding.tvParkingStatus.context.getColor(R.color.grey))
            }
            binding.tvVehiclePlateNo.text = item.plateNo

            binding.tvParkingLot.text = binding.tvParkingLot.context.getString(
                R.string.parking_lot_label,
                item.parkingLotId
            )
            binding.tvParkingFloorAndSpace.text = binding.tvParkingLot.context.getString(
                R.string.floor_and_space,
                item.parkingFloorId,
                item.parkingSpaceId
            )
            binding.tvInTime.text = binding.tvInTime.context.getString(
                R.string.billing_in_time_label,
                Common.getDateAndTime(item.inTime)
            )


        }

    }


}