package com.elamparithi.parkypark.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.elamparithi.parkypark.utils.AuthenticationState
import com.elamparithi.parkypark.utils.FirebaseUserLiveData

abstract class BaseViewModel : ViewModel() {
    val toastStringMLD: MutableLiveData<String> = MutableLiveData()
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun getToast() = toastStringMLD

}