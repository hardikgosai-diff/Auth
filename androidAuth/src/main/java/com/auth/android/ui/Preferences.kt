package com.auth.android.ui

import android.content.Context
import com.auth.android.models.User
import com.google.gson.Gson

/**
 * 07/05/25.
 *
 * @author hardkgosai.
 */
class Preferences(context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences("Auth", Context.MODE_PRIVATE)
    }

    var user: User
        get() {
            val user = preferences.getString("user", "")
            return if (!user.isNullOrEmpty()) Gson().fromJson(user, User::class.java) else User()
        }
        set(value) {
            preferences.edit().putString("user", Gson().toJson(value)).apply()
        }

    var rememberMe: Boolean
        get() = preferences.getBoolean("rememberMe", false)
        set(value) {
            preferences.edit().putBoolean("rememberMe", value).apply()
        }

}