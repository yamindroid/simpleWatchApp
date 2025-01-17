package com.ymo.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import com.ymo.BuildConfig.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesHelper @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var lastPageNumber: Int
        get() = sharedPreferences.getInt("LAST_PAGE_NUMBER", 1)
        set(value) = sharedPreferences.edit().putInt("LAST_PAGE_NUMBER", value).apply()
}