package com.auth.android.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 07/05/25.
 *
 * @author hardkgosai.
 */
data class User(
    @SerializedName("name") private var name: String? = null,
    @SerializedName("email") private var email: String? = null,
    @SerializedName("password") private var password: String? = null
) : Serializable {

    val safeName: String
        get() = name ?: ""

    val safeEmail: String
        get() = email ?: ""

    val safePassword: String
        get() = password ?: ""
}