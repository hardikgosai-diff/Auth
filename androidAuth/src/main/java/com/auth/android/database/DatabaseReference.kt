package com.auth.android.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * 06/01/25.
 *
 * @author hardkgosai.
 */
object DatabaseReference {

    private val databaseReference: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference

    fun usersRef() = databaseReference.child("users")

}