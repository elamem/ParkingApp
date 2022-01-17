package com.elamparithi.parkypark.ui.bookingHistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.entity.VehicleType
import com.elamparithi.parkypark.databinding.FragmentBookingHistoryBinding
import com.elamparithi.parkypark.ui.base.BaseFragment
import com.elamparithi.parkypark.ui.bookingHistory.bookingHistoryAdapter.BookingHistoryAdapter
import com.elamparithi.parkypark.ui.home.HomeCallback
import com.elamparithi.parkypark.ui.pickup.PickUpFragment
import com.elamparithi.parkypark.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingHistoryFragment :
    BaseFragment<BookingHistoryViewModel, FragmentBookingHistoryBinding>() {

    private lateinit var historyAdapter: BookingHistoryAdapter
    private var homeCallback: HomeCallback? = null

    companion object {
        @JvmStatic
        fun newInstance() =
            BookingHistoryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getParkingDetailsBottomSheetCurrentState =
            homeCallback?.getParkingDetailsBottomSheetCurrentState()!!
        homeCallback?.handleParkingDetailsBottomSheet(BottomSheetBehavior.STATE_HIDDEN)
    }

    override fun getViewModelClass() = BookingHistoryViewModel::class.java

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBookingHistoryBinding.inflate(inflater, container, false)

    override fun setupView(view: View) {
        historyAdapter = BookingHistoryAdapter { bookingDetail ->
            var shouldCalculatePayment = false
            if (bookingDetail.bookingStatus == Constants.BOOKING_STATUS_ACTIVE) {
                shouldCalculatePayment = true
            }
            PickUpFragment.newInstance(bookingDetail, shouldCalculatePayment)
                .show(childFragmentManager, "payment_summary")
        }
        binding.rvBookingHistory.adapter = historyAdapter
        binding.atvVehicleType.setOnItemClickListener { parent, _, position, _ ->
            val selectedVehicleType = parent?.getItemAtPosition(position) as VehicleType
            if (selectedVehicleType.vehicleTypeName == getString(R.string.vehicleTypeAll)) {
                val firstItem = parent.getItemAtPosition(0) as VehicleType
                val lastItem = parent.getItemAtPosition(parent.count - 1) as VehicleType
                viewModel.updateSelectedVehicleTypeRange(
                    firstItem.vehicleTypeId,
                    lastItem.vehicleTypeId
                )
            } else
                viewModel.updateSelectedVehicleTypeRange(
                    selectedVehicleType.vehicleTypeId,
                    selectedVehicleType.vehicleTypeId
                )
        }

    }

    override fun observeData() {

        viewModel.getAvailableBookingList().observe(viewLifecycleOwner) { bookingList ->
            if (bookingList.isEmpty()) {
                binding.tvEmptyBookingsLabel.visibility = View.VISIBLE
                binding.rvBookingHistory.visibility = View.GONE
            } else {
                binding.tvEmptyBookingsLabel.visibility = View.GONE
                binding.rvBookingHistory.visibility = View.VISIBLE
            }
            historyAdapter.submitList(bookingList)
        }

        viewModel.vehicleTypeListLD.observe(viewLifecycleOwner) { vehicleTypeList ->
            vehicleTypeList.add(0, VehicleType(getString(R.string.vehicleTypeAll), -1, -1F, -1f))
            val arrayAdapter: ArrayAdapter<VehicleType> =
                ArrayAdapter<VehicleType>(
                    requireContext(),
                    R.layout.item_list_drop_down_text,
                    vehicleTypeList
                )
            binding.atvVehicleType.setAdapter<ArrayAdapter<VehicleType>>(arrayAdapter)
            if (binding.atvVehicleType.text.isEmpty()) {
                binding.atvVehicleType.setSelection(0)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeCallback) {
            homeCallback = context
        } else {
            throw RuntimeException("$context must implement HomeCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        homeCallback = null
    }

    override fun onDestroy() {
        homeCallback?.handleParkingDetailsBottomSheet(viewModel.getParkingDetailsBottomSheetCurrentState)
        super.onDestroy()
    }
}