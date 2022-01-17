package com.elamparithi.parkypark.ui.coupon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.elamparithi.parkypark.data.repository.CouponRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import com.elamparithi.parkypark.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val couponRepository: CouponRepository
) : BaseViewModel() {

    fun getCouponListForGivenUser() =
        Transformations.switchMap(
            couponRepository.getCurrentActiveUserId()
        ) { userId ->
            userId?.let {
                couponRepository.getCouponListForGivenUser(it, Constants.COUPON_STATUS_ACTIVE)
            }
        }

    fun checkTheGivenCouponValidity(givenCode: String) =
        couponRepository.getCouponByGivenCode(givenCode)


}