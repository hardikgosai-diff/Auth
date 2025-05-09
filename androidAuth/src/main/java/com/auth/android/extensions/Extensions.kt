package com.auth.android.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast

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

    fun Activity.toast(message: String) {
        withTryCatch {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun Activity.toast(message: Int) {
        withTryCatch {
            Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
        }
    }

}