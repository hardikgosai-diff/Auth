package com.auth.android.activities.signup

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.auth.android.R
import com.auth.android.activities.dashboard.DashboardActivity
import com.auth.android.database.AuthReference.currentUser
import com.auth.android.database.AuthReference.firebaseAuth
import com.auth.android.database.DatabaseReference.usersRef
import com.auth.android.extensions.Extensions.openActivity
import com.auth.android.extensions.Extensions.toast
import com.auth.android.models.User
import com.auth.android.utils.GoogleSignIn

/**
 * 12/05/25.
 *
 * @author hardkgosai.
 */
abstract class SignUpBase : ComponentActivity() {

    var showProgress by mutableStateOf(false)
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var showNameError by mutableStateOf(false)
    var showEmailError by mutableStateOf(false)
    var showPasswordError by mutableStateOf(false)
    var showConfirmPasswordError by mutableStateOf(false)
    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)
    var isNameFocused by mutableStateOf(false)
    var isEmailFocused by mutableStateOf(false)
    var isPasswordFocused by mutableStateOf(false)
    var isConfirmPasswordFocused by mutableStateOf(false)
    var isTermsChecked by mutableStateOf(false)

    var googleSignIn: GoogleSignIn? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        googleSignIn = GoogleSignIn(this) { authResult ->
            if (authResult.isSuccessful) {
                checkUserExistenceFirebase()
            } else {
                showProgress = false
                toast("${authResult.exception?.localizedMessage}")
            }
        }
    }

    fun onEmailPasswordSignUp() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = User(
                    uid = currentUser?.uid,
                    name = name,
                    email = currentUser?.email,
                    photoUrl = currentUser?.photoUrl.toString(),
                    createdAt = System.currentTimeMillis()
                )
                saveUserToFirebase(user)
            } else {
                showProgress = false
                toast(task.exception?.localizedMessage ?: getString(R.string.error))
            }
        }
    }

    private fun checkUserExistenceFirebase() {
        currentUser?.uid?.let {
            usersRef().child(it).get().addOnCompleteListener { snapshot1 ->
                if (snapshot1.result.exists()) {
                    showProgress = false
                    openActivity(DashboardActivity::class.java)
                    finishAffinity()
                } else {
                    val user = User(
                        uid = currentUser?.uid,
                        name = currentUser?.displayName,
                        email = currentUser?.email,
                        photoUrl = currentUser?.photoUrl.toString(),
                        createdAt = System.currentTimeMillis()
                    )
                    saveUserToFirebase(user)
                }
            }
        } ?: run {
            showProgress = false
            toast(getString(R.string.not_logged_in_yet))
        }
    }

    private fun saveUserToFirebase(user: User) {
        usersRef().child(user.safeUid).setValue(user).addOnCompleteListener { snapshot2 ->
            showProgress = false
            if (snapshot2.isSuccessful) {
                toast("success")
            } else {
                toast(snapshot2.exception?.message ?: getString(R.string.error))
            }
        }
    }

    fun isEmailValid(): Boolean {
        showEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return !showEmailError
    }

    fun isPasswordValid(): Boolean {
        showPasswordError = !Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#^+=._-]).{6,}$"
        ).matches(password)

        if (showPasswordError) return false

        showConfirmPasswordError = !Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#^+=._-]).{6,}$"
        ).matches(confirmPassword) || password != confirmPassword

        return !showConfirmPasswordError
    }

}