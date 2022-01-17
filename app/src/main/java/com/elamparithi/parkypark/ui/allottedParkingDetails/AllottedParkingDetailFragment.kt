package com.elamparithi.parkypark.ui.allottedParkingDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.databinding.FragmentAllottedParkingDetailBinding
import com.elamparithi.parkypark.ui.base.BaseFragment
import com.elamparithi.parkypark.utils.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment helps to display allotted parking details to the user.
 * User can verify everything and confirm the booking.
 * if user reached the parking lot they can scan the QR code (did't implement the QR logic) and initiate the parking
 */
@AndroidEntryPoint
class AllottedParkingDetailFragment :
    BaseFragment<AllottedParkingDetailViewModel, FragmentAllottedParkingDetailBinding>() {


    companion object {
        fun newInstance() = AllottedParkingDetailFragment()
    }

    override fun getViewModelClass() = AllottedParkingDetailViewModel::class.java

    override fun setupView(view: View) {

        //to confirm the booking
        binding.btnConfirmBooking.setOnClickListener {
            viewModel.confirmTheGivenBooking()
        }

        //to cancel the booking
        binding.btnCancel.setOnClickListener {
            viewModel.cancelTheGivenBooking()
        }

        //to scan and park
        binding.btnScanAndPark.setOnClickListener {
            viewModel.activateTheGivenBooking()
        }
    }

    override fun observeData() {

        // this single observer is responsible to show all the details about the vehicle, parking lot details etc.
        viewModel.getCurrentlyActiveBookingFullDetails()
            .observe(viewLifecycleOwner) { bookingDetail ->

                bookingDetail?.let {
                    binding.tvParkingTypeLabel.apply {
                        text = if (bookingDetail.parkingType == Constants.PARKING_TYPE_PARK_NOW) {
                            getString(R.string.park_now)
                        } else {
                            getString(R.string.reserve_at)
                        }
                    }

                    binding.layoutVehicleType.ivVehicleTypeIcon.setImageResource(bookingDetail.vehicleIcon)
                    binding.layoutVehicleType.tvVehicleTypeName.text = bookingDetail.vehicleTypeName
                    binding.layoutVehicleType.tvVehiclePlateNo.text = bookingDetail.plateNo
                    binding.layoutVehicleType.tvVehicleSerialNo.text =
                        getString(R.string.vehicle_label, bookingDetail.vehicleId)
                    binding.layoutVehicleType.viewGroupFeeDetails.visibility = View.VISIBLE
                    binding.layoutVehicleType.tvFirstHourFee.text =
                        getString(R.string.first_hour_price_label, bookingDetail.priceFirstHour)
                    binding.layoutVehicleType.tvRemainingHourFee.text =
                        getString(
                            R.string.remaining_hour_price_label,
                            bookingDetail.priceRemainingHour
                        )

                    binding.layoutParkingLotDetails.tvParkingLotAddress.text =
                        getString(R.string.parking_lot_label, bookingDetail.parkingLotId)
                    binding.layoutParkingLotDetails.tvParkingFloorId.text =
                        bookingDetail.parkingFloorId.toString()
                    binding.layoutParkingLotDetails.tvParkingSpaceId.text =
                        bookingDetail.parkingSpaceId.toString()
                    binding.layoutParkingLotDetails.tvDistance.text =
                        getString(R.string.distance_label, bookingDetail.distance)

                    if (bookingDetail.couponId > 0) {
                        binding.layoutCoupon.root.visibility = View.VISIBLE
                        binding.layoutCoupon.tvCouponName.text =
                            getString(R.string.coupon_prefix, bookingDetail.name)
                        binding.layoutCoupon.tvCouponStatus.text = getString(R.string.applied)
                        binding.layoutCoupon.tvCouponStatus.visibility = View.VISIBLE
                        binding.layoutCoupon.tvDiscountFirstHour.text =
                            getString(
                                R.string.first_hour_off,
                                bookingDetail.discountPercentFirstHour
                            )
                        binding.layoutCoupon.tvDiscountRemainingHours.text = getString(
                            R.string.remaining_hour_off,
                            bookingDetail.discountPercentRemainingHour
                        )
                    } else {
                        binding.layoutCoupon.root.visibility = View.GONE
                    }

                    if (bookingDetail.bookingStatus == Constants.BOOKING_STATUS_IN_PROGRESS) {
                        binding.groupConfirmBooking.visibility = View.VISIBLE
                        binding.groupScanAndPark.visibility = View.GONE
                        binding.layoutVehicleType.root.visibility = View.VISIBLE
                    } else if (bookingDetail.bookingStatus == Constants.BOOKING_STATUS_CONFIRMED) {
                        binding.groupConfirmBooking.visibility = View.GONE
                        binding.groupScanAndPark.visibility = View.VISIBLE
                        binding.layoutVehicleType.root.visibility = View.GONE
                        binding.layoutCoupon.root.visibility = View.GONE
                    }
                }

            }


        viewModel.getToast().observe(viewLifecycleOwner) {
            it?.let { responseStr ->
                Snackbar.make(binding.root, responseStr, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAllottedParkingDetailBinding {
        return FragmentAllottedParkingDetailBinding.inflate(inflater, container, false)
    }

}