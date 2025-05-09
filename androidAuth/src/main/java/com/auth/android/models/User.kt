package com.auth.android.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 07/05/25.
 *
 * @author hardkgosai.
 */
data class User(
    @SerializedName("uid") private var uid: String? = null,
    @SerializedName("name") private var name: String? = null,
    @SerializedName("email") private var email: String? = null,
    @SerializedName("photoUrl") private var photoUrl: String? = null,
    @SerializedName("createdAt") private var createdAt: Long? = 0L
) : Serializable {

    val safeUid: String
        get() = uid ?: ""

    val safeName: String
        get() = name ?: ""

    val safeEmail: String
        get() = email ?: ""

    val safePhotoUrl: String
        get() = photoUrl ?: ""

    val safeCreatedAt: Long
        get() = createdAt ?: 0L

}