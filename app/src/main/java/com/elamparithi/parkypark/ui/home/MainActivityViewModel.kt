package com.elamparithi.parkypark.ui.home

import androidx.lifecycle.Transformations
import com.elamparithi.parkypark.data.local.database.DatabaseService
import com.elamparithi.parkypark.data.local.database.entity.Booking
import com.elamparithi.parkypark.data.local.database.entity.CouponUserRel
import com.elamparithi.parkypark.data.local.database.entity.Location
import com.elamparithi.parkypark.data.local.database.entity.User
import com.elamparithi.parkypark.data.repository.HomeRepository
import com.elamparithi.parkypark.data.repository.LoginRepository
import com.elamparithi.parkypark.ui.base.BaseViewModel
import com.elamparithi.parkypark.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    databaseService: DatabaseService,
    private val coroutineScope: CoroutineScope,
    private val loginRepository: LoginRepository,
    private val homeRepository: HomeRepository
) : BaseViewModel() {

    var bookingDetails: Booking? = null
    private val subscribe: Disposable

    init {
        Timber.e("created")
        subscribe = databaseService.userDao().getAllUsers().subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                if (it.isEmpty()) {
                    Timber.e("is empty")
                } else {
                    Timber.e("is empty" + it.size)
                }
            }, {
                Timber.e(it.message.toString())
            })
    }

    fun insertUserDetails() {
        coroutineScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let {
                if (it.displayName != null && it.email != null) {
                    val userId = loginRepository.insertUserDetails(
                        User(
                            it.displayName!!,
                            it.email!!,
                            Location(13.084525182485894, 80.27439852711612),
                            true
                        )
                    )
                    loginRepository.insertUserCoupon(
                        CouponUserRel(
                            1,
                            userId,
                            Constants.COUPON_STATUS_ACTIVE
                        )
                    )
                }
            }
        }
    }

    fun clearUserDetails() {
        coroutineScope.launch {
            loginRepository.clearUserDetails()
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscribe.dispose()
    }

    fun getCurrentlyActiveOrInProgressBookingDetails() =
        Transformations.distinctUntilChanged(homeRepository.getCurrentlyActiveOrInProgressBookingDetails())

}