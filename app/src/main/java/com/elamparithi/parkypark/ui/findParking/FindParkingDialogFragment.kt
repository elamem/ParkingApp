package com.elamparithi.parkypark.ui.findParking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.databinding.FragmentFindParkingBinding
import com.elamparithi.parkypark.ui.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class FindParkingDialogFragment :
    BaseBottomSheetDialogFragment<FindParkingViewModel, FragmentFindParkingBinding>() {

    companion object {
        const val KEY_LOADER_IS_CANCELABLE = "isCancelable"
        const val KEY_LOADER_LABEL = "loaderLabel"
        const val KEY_BOOKING_DETAILS = "booking_details"

        @JvmStatic
        fun newInstance(loaderLabel: String, isCancelable: Boolean, booking: Booking) =
            FindParkingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_LOADER_LABEL, loaderLabel)
                    putBoolean(KEY_LOADER_IS_CANCELABLE, isCancelable)
                    putParcelable(KEY_BOOKING_DETAILS, booking)
                }
            }
    }

    override fun getViewModelClass() = FindParkingViewModel::class.java

    override fun setupView(view: View) {
        dialog?.setCancelable(viewModel.getIsCancelable())
        binding.tvLoaderLabel.text = viewModel.getLoaderLabel()
    }

    override fun observeData() {
        viewModel.bookingIdMLD.observe(viewLifecycleOwner) {
            Timber.e("dsa$it")
            dismiss()
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFindParkingBinding.inflate(inflater, container, false)


}