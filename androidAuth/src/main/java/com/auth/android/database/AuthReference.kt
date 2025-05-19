package com.auth.android.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * 06/01/25.
 *
 * @author hardkgosai.
 */
object AuthReference {

    val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

}