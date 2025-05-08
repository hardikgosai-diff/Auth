package com.auth.android.extensions

import android.app.Activity
import android.content.Intent

/**
 * 07/05/25.
 *
 * @author hardkgosai.
 */
object Extensions {

    inline fun <T> withTryCatch(action: () -> T): T? {
        return try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun Activity.openActivity(it: Class<*>, action: String? = null) {
        val intent = Intent(this, it)
        intent.action = action
        startActivity(intent)
    }

}