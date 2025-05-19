package com.auth.android.models

import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 07/05/25.
 *
 * @author hardkgosai.
 */
data class User(
    @SerializedName("uid") var uid: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("photoUrl") var photoUrl: String? = null,
    @SerializedName("createdAt") var createdAt: Long? = 0L
) : Serializable {

    @get:Exclude
    val safeUid: String
        get() = uid ?: ""

    @get:Exclude
    val safeName: String
        get() = name ?: ""

    @get:Exclude
    val safeEmail: String
        get() = email ?: ""

    @get:Exclude
    val safePhotoUrl: String
        get() = photoUrl ?: ""

    @get:Exclude
    val safeCreatedAt: Long
        get() = createdAt ?: 0L

}