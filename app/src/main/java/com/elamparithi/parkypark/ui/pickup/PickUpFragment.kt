package com.elamparithi.parkypark.ui.pickup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.model.BookingHistory
import com.elamparithi.parkypark.databinding.FragmentPickUpBinding

import com.elamparithi.parkypark.ui.base.BaseBottomSheetDialogFragment
import com.elamparithi.parkypark.utils.Common
import com.elamparithi.parkypark.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickUpFragment : BaseBottomSheetDialogFragment<PickUpViewModel, FragmentPickUpBinding>() {

    companion object {
        const val KEY_BOOKING_DETAIL = "bookingDetail"
        const val KEY_CALCULATE_PAYMENT = "calculatePayment"
        fun newInstance(bookingDetail: BookingHistory, shouldCalculatePayment: Boolean = false) =
            PickUpFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_BOOKING_DETAIL, bookingDetail)
                    putBoolean(KEY_CALCULATE_PAYMENT, shouldCalculatePayment)
                }
            }
    }

    override fun getViewModelClass() = PickUpViewModel::class.java

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPickUpBinding.inflate(inflater, container, false)

    override fun setupView(view: View) {
        binding.layoutVehicleType.tvVehicleTypeName.text = viewModel.bookingDetail.vehicleTypeName
        binding.layoutVehicleType.ivVehicleTypeIcon.setImageResource(viewModel.bookingDetail.vehicleIcon)
        binding.tvVehiclePlateNo.text = viewModel.bookingDetail.plateNo
        binding.tvParkingLot.text = binding.tvParkingLot.context.getString(
            R.string.parking_lot_label,
            viewModel.bookingDetail.parkingLotId
        )
        binding.tvParkingFloorAndSpace.text = binding.tvParkingLot.context.getString(
            R.string.floor_and_space,
            viewModel.bookingDetail.parkingFloorId,
            viewModel.bookingDetail.parkingSpaceId
        )
        binding.tvInTime.text = binding.tvInTime.context.getString(
            R.string.billing_in_time_label,
            Common.getDateAndTime(viewModel.bookingDetail.inTime)
        )
        binding.btnPayAndPickUp.setOnClickListener {
            viewModel.releaseTheVehicle()
        }
    }

    override fun observeData() {

        viewModel.paymentDetailMLD.observe(viewLifecycleOwner) {
            it?.let { payment ->
                binding.tvFirstHourPayment.root.text =
                    getString(R.string.first_hour_payment_label, payment.firstHourAmount)
                binding.tvRemainingHourPayment.root.text =
                    getString(R.string.remaining_hour_payment_label, payment.remainingHourAmount)
                binding.tvFirstHourDiscount.root.text =
                    getString(R.string.first_hour_discount_label, payment.firstHourDiscount)
                binding.tvRemainingHourDiscount.root.text =
                    getString(R.string.remaining_hour_discount_label, payment.remainingHourDiscount)
                binding.tvCancellationFee.root.text =
                    getString(R.string.cancellation_fee_label, payment.cancellationFee)
                binding.tvReservationFee.root.text =
                    getString(R.string.reservation_fee_label, payment.reservationFee)
                binding.tvTotal.root.text = getString(R.string.total_fee_label, payment.total)
            }
        }
        viewModel.getCurrentBookingOutTime().observe(viewLifecycleOwner) {
            it?.let { outTime ->
                binding.tvOutTime.text = binding.tvInTime.context.getString(
                    R.string.billing_out_time_label,
                    Common.getDateAndTime(outTime)
                )
            }
        }
        viewModel.getCurrentBookingStatus().observe(viewLifecycleOwner) {
            it?.let { bookingStatus ->
                binding.tvParkingStatus.text = Common.getParkingStatusLabel(
                    bookingStatus,
                    requireContext().applicationContext
                )

                if (bookingStatus == Constants.BOOKING_STATUS_ACTIVE) {
                    binding.tvParkingStatus.setTextColor(binding.tvParkingStatus.context.getColor(R.color.green))
                } else {
                    binding.tvParkingStatus.setTextColor(binding.tvParkingStatus.context.getColor(R.color.grey))
                }
            }
        }

        viewModel.getCurrentPaymentStatus().observe(viewLifecycleOwner) {
            it?.let { paymentStatus ->
                when (paymentStatus) {
                    Constants.PAYMENT_STATUS_IN_DUE -> {
                        binding.btnPayAndPickUp.visibility = View.VISIBLE
                        binding.tvAfterPickupMessage.visibility = View.GONE
                    }
                    Constants.PAYMENT_STATUS_PAID -> {
                        binding.btnPayAndPickUp.visibility = View.GONE
                        binding.tvAfterPickupMessage.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.btnPayAndPickUp.visibility = View.GONE
                        binding.tvAfterPickupMessage.visibility = View.GONE
                    }
                }
                binding.tvPaymentStatus.text = getString(
                    R.string.payment_status, Common.getPaymentStatusLabel(
                        paymentStatus,
                        requireContext().applicationContext
                    )
                )
            }
        }
    }

}