package com.auth.android.database

import com.google.firebase.auth.FirebaseAuth

/**
 * 06/01/25.
 *
 * @author hardkgosai.
 */
object AuthReference {

    val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    val currentUser by lazy { firebaseAuth.currentUser }

    val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

}