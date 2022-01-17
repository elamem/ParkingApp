package com.elamparithi.parkypark.ui.getParkingDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.databinding.FragmentGetParkingDetailsBinding
import com.elamparithi.parkypark.ui.base.BaseFragment
import com.elamparithi.parkypark.ui.coupon.CouponFragment
import com.elamparithi.parkypark.ui.getParkingDetails.vehicleAdapter.VehicleAdapter
import com.elamparithi.parkypark.ui.getParkingDetails.vehicleTypeAdapter.VehicleTypeAdapter
import com.elamparithi.parkypark.ui.home.HomeCallback
import com.elamparithi.parkypark.utils.AuthenticationState
import com.elamparithi.parkypark.utils.Common
import com.elamparithi.parkypark.utils.Constants
import com.elamparithi.parkypark.utils.ItemDecorationMargin
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GetParkingDetailsFragment :
    BaseFragment<GetParkingDetailsViewModel, FragmentGetParkingDetailsBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() =
            GetParkingDetailsFragment()
    }

    private var homeCallback: HomeCallback? = null
    private lateinit var vehicleTypeAdapter: VehicleTypeAdapter
    private lateinit var vehicleAdapter: VehicleAdapter

    override fun getViewModelClass() = GetParkingDetailsViewModel::class.java

    override fun setupView(view: View) {
        viewModel.updateSelectedVehicleTypeId(-1)
        vehicleTypeAdapter = VehicleTypeAdapter { id, name, position ->
            viewModel.updateSelectedVehicleTypeId(id)
            viewModel.selectedVehicleTypeName = name
            viewModel.selectedVehicleTypePosition = position
        }
        vehicleAdapter = VehicleAdapter { VehicleWithVehicleType ->
            viewModel.setSelectedVehicleId(VehicleWithVehicleType.vehicleId)
        }
        binding.rvVehicleType.adapter = vehicleTypeAdapter
        binding.rvVehicleList.adapter = vehicleAdapter
        binding.rvVehicleType.addItemDecoration(
            ItemDecorationMargin(
                resources.getDimension(R.dimen.vehicleTypeMargin).toInt()
            )
        )
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
        binding.btnToggleBookingType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (checkedId == binding.btnParkNow.id && isChecked) {
                //park now
                binding.btnReservation.text = getString(R.string.reservation)
                viewModel.setSelectedParkingType(Constants.PARKING_TYPE_PARK_NOW)
                return@addOnButtonCheckedListener
            }
            if (checkedId == binding.btnReservation.id && isChecked) {
                //reservation
                datePicker.show(childFragmentManager, "date_dialog")
                return@addOnButtonCheckedListener
            }
        }

        datePicker.addOnPositiveButtonClickListener { timeStamp ->
            val str = getString(R.string.reserve_at) + " " + Common.getDate(timeStamp)
            binding.btnReservation.text = str
            viewModel.setSelectedParkingType(Constants.PARKING_TYPE_RESERVATION)
        }

        binding.btnAddNewVehicle.setOnClickListener {

            if (binding.etVehiclePlateNo.text.toString().isNotEmpty()) {
                if (viewModel.authenticationState.value == AuthenticationState.AUTHENTICATED) {
                    //user already logged in
                    viewModel.insertNewVehicle(binding.etVehiclePlateNo.text.toString())
                    Common.hideKeyboard(binding.root, requireContext())
                } else {
                    //yet to login
                    homeCallback?.callUserLogin()
                }

            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.enter_a_valid_plate_number),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.btnFindParking.setOnClickListener {

            if (!Common.isOnline(requireContext())) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.internet_required),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (viewModel.getSelectedVehicleId() == -1L) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.selecte_a_vehicle),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (viewModel.getSelectedVehicleTypeId() == -1L) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.select_a_vehicle_type),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (viewModel.selectedParkingTypeValue == Constants.PARKING_TYPE_RESERVATION) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.reservation_not_available),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            homeCallback?.checkLocationAccess(viewModel.getBookingDetails())
        }

        binding.btnAddCoupon.setOnClickListener {

            if (viewModel.authenticationState.value == AuthenticationState.AUTHENTICATED) {
                //already logged in
                //add coupon
                val couponFragment = CouponFragment.newInstance { selectedCoupon ->
                    Timber.e(selectedCoupon.couponCode)
                    viewModel.setSelectedCoupon(selectedCoupon)
                    binding.layoutCoupon.tvCouponName.text =
                        getString(R.string.coupon_prefix, selectedCoupon.couponName)
                    binding.layoutCoupon.tvCouponStatus.text = getString(R.string.applied)
                    binding.layoutCoupon.tvDiscountFirstHour.text =
                        getString(R.string.first_hour_off, selectedCoupon.discountPercentFirstHour)
                    binding.layoutCoupon.tvDiscountRemainingHours.text =
                        getString(
                            R.string.remaining_hour_off,
                            selectedCoupon.discountPercentRemainingHour
                        )
                    binding.layoutCoupon.tvCouponStatus.visibility = View.VISIBLE
                    binding.layoutCoupon.root.visibility = View.VISIBLE
                    binding.btnAddCoupon.visibility = View.GONE
                }
                couponFragment.show(childFragmentManager, "coupon_dialog")
            } else {
                //yet to login
                homeCallback?.callUserLogin()
            }

        }

        binding.layoutCoupon.ivRemove.setOnClickListener {
            //remove applied coupon
            viewModel.setSelectedCoupon(null)
            binding.btnAddCoupon.visibility = View.VISIBLE
            binding.layoutCoupon.root.visibility = View.GONE
        }

    }

    private fun updateFindParkingFullViewVisibility(visibility: Int) {

        if (visibility == View.VISIBLE) {
            homeCallback?.handleParkingDetailsBottomSheet(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            homeCallback?.handleParkingDetailsBottomSheet(BottomSheetBehavior.STATE_COLLAPSED)
        }
        binding.clSecondPhaseViews.visibility = visibility
    }

    override fun observeData() {
        viewModel.getAvailableVehicleTypes().observe(viewLifecycleOwner, { vehicleTypeList ->
            vehicleTypeAdapter.submitList(vehicleTypeList)
        })
        viewModel.getUserVehicleList().observe(viewLifecycleOwner, { vehicleList ->
            if (vehicleList.isEmpty()) {
                binding.rvVehicleList.visibility = View.GONE
                binding.tvEmptyVehicleLabel.visibility = View.VISIBLE
            } else {
                binding.rvVehicleList.visibility = View.VISIBLE
                binding.tvEmptyVehicleLabel.visibility = View.GONE
            }
            vehicleAdapter.submitList(vehicleList)
        })
        viewModel.getToast().observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, { authenticationState ->
            authenticationState?.let {
                if (it == AuthenticationState.UNAUTHENTICATED) {
                    viewModel.updateSelectedVehicleTypeId(-1)
                    clearUI()
                }
            }
        })

        viewModel.selectedVehicleTypeIdMLD.observe(viewLifecycleOwner) { selectedVehicleTypeId ->
            selectedVehicleTypeId?.let {
                if (it == -1L)
                    updateFindParkingFullViewVisibility(View.GONE)
                else
                    updateFindParkingFullViewVisibility(View.VISIBLE)
            } ?: run {
                updateFindParkingFullViewVisibility(View.GONE)
            }
        }
    }

    private fun clearUI() {
        if (viewModel.selectedVehicleTypePosition != -1 && viewModel.selectedVehicleTypePosition < vehicleTypeAdapter.itemCount) {
            vehicleTypeAdapter.setSelectedVehicleTypePositionAndLastSelectedPosition(-1, -1)
            vehicleTypeAdapter.notifyItemChanged(viewModel.selectedVehicleTypePosition)
            viewModel.selectedVehicleTypePosition = -1
        }
        binding.etVehiclePlateNo.text.clear()
        binding.btnToggleBookingType.check(R.id.btnParkNow)
        binding.layoutCoupon.root.visibility = View.GONE
        binding.btnAddCoupon.visibility = View.VISIBLE

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

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGetParkingDetailsBinding {
        return FragmentGetParkingDetailsBinding.inflate(inflater, container, false)
    }

}