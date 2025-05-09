package com.auth.android.activities.signIn

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.auth.android.database.AuthReference.currentUser
import com.auth.android.database.AuthReference.firebaseAuth
import com.auth.android.database.DatabaseReference.usersRef
import com.auth.android.extensions.Extensions.toast
import com.auth.android.models.User
import com.auth.android.ui.Preferences
import com.auth.android.utils.GoogleSignIn

/**
 * 09/05/25.
 *
 * @author hardkgosai.
 */
abstract class SignInBase : ComponentActivity() {

    val preferences by lazy { Preferences(this) }

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var showEmailError by mutableStateOf(false)
    var showPasswordError by mutableStateOf(false)
    var passwordVisible by mutableStateOf(false)
    var rememberMe by mutableStateOf(false)
    var isEmailFocused by mutableStateOf(false)
    var isPasswordFocused by mutableStateOf(false)
    var showProgress by mutableStateOf(false)

    var googleSignIn: GoogleSignIn? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        googleSignIn = GoogleSignIn(this) { authResult ->
            if (authResult.isSuccessful) {
                saveUserToFirebase()
            } else {
                showProgress = false
                toast("${authResult.exception?.localizedMessage}")
            }
        }
    }

    fun onEmailPasswordSignIn() {
        showProgress = true
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveUserToFirebase()
            } else {
                showProgress = false
                toast("${task.exception?.localizedMessage}")
            }
        }
    }

    private fun saveUserToFirebase() {
        val user = User(
            uid = currentUser?.uid,
            name = currentUser?.displayName,
            email = currentUser?.email,
            photoUrl = currentUser?.photoUrl.toString(),
            createdAt = System.currentTimeMillis()
        )

        usersRef().child(user.safeUid).setValue(user).addOnSuccessListener {
            showProgress = false
            toast("success")
            //openActivity(DashboardActivity::class.java)
        }
    }

    fun isEmailPasswordValid(): Boolean {
        showEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (showEmailError) return false

        showPasswordError = !Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#^+=._-]).{6,}$"
        ).matches(password)

        return !showPasswordError
    }

}