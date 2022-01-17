package com.elamparithi.parkypark.data.local.prefs

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(private val prefs: SharedPreferences)  {

    companion object  {
        const val KEY_USER_ID = "PREF_KEY_USER_ID"
    }

    fun getUserId(): Long =
        prefs.getLong(KEY_USER_ID, -1)

    fun setUserId(userId: Long) =
        prefs.edit().putLong(KEY_USER_ID, userId).apply()

    fun removeUserId() =
        prefs.edit().remove(KEY_USER_ID).apply()
}