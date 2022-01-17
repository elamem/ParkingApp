package com.elamparithi.parkypark.ui.coupon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elamparithi.parkypark.R
import com.elamparithi.parkypark.data.local.database.entity.Coupon
import com.elamparithi.parkypark.databinding.FragmentCouponBinding
import com.elamparithi.parkypark.ui.base.BaseBottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class CouponFragment(private val onSelectedCoupon: (Coupon) -> Unit) :
    BaseBottomSheetDialogFragment<CouponViewModel, FragmentCouponBinding>() {

    private lateinit var couponAdapter: CouponAdapter

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    companion object {
        @JvmStatic
        fun newInstance(selectedCoupon: (Coupon) -> Unit) =
            CouponFragment(selectedCoupon)
    }

    override fun getViewModelClass() = CouponViewModel::class.java

    override fun setupView(view: View) {
        couponAdapter = CouponAdapter { selectedCoupon ->
            onSelectedCoupon(selectedCoupon)
            dismiss()
        }
        binding.rvAvailableCoupons.adapter = couponAdapter

        binding.btnVerifyCoupon.setOnClickListener {
            if (binding.etCouponCode.text.isNotEmpty()) {
                verifyGivenCouponCode(binding.etCouponCode.text.toString())
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.enter_valid_coupon_code),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun verifyGivenCouponCode(givenCode: String) {
        val checkTheGivenCouponValidityDisposable = viewModel.checkTheGivenCouponValidity(
            givenCode
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    onSelectedCoupon(it)
                    dismiss()
                }
            }, {
                Snackbar.make(
                    binding.root,
                    getString(R.string.enter_valid_coupon_code),
                    Snackbar.LENGTH_SHORT
                ).show()
                dismiss()
            })

        compositeDisposable.add(checkTheGivenCouponValidityDisposable)
    }

    override fun observeData() {
        viewModel.getCouponListForGivenUser().observe(viewLifecycleOwner) { couponList ->
            if (couponList.isEmpty()) {
                binding.tvCouponsErrorLabel.visibility = View.VISIBLE
                binding.rvAvailableCoupons.visibility = View.GONE
            } else {
                binding.tvCouponsErrorLabel.visibility = View.GONE
                binding.rvAvailableCoupons.visibility = View.VISIBLE
            }
            couponAdapter.submitList(couponList)
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCouponBinding {
        return FragmentCouponBinding.inflate(inflater, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}