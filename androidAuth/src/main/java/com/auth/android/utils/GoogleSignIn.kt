package com.auth.android.utils

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.auth.android.R
import com.auth.android.extensions.Extensions.withTryCatch
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * 03/01/25.
 *
 * @author hardkgosai.
 */
class GoogleSignIn(
    private val activity: ComponentActivity,
    private val onCompleteListener: OnCompleteListener<AuthResult>
) {

    private val oneTapClient by lazy { Identity.getSignInClient(activity) }

    private val signInLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleResult(result)
        }

    fun signIn() {
        val signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(activity.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false).build()
        ).setAutoSelectEnabled(true) // Auto-select account if available
            .build()

        oneTapClient.beginSignIn(signInRequest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                signInLauncher.launch(
                    IntentSenderRequest.Builder(task.result.pendingIntent.intentSender).build()
                )
            } else {
                onCompleteListener.onComplete(Tasks.forCanceled())
            }
        }
    }

    fun handleResult(result: ActivityResult) {
        withTryCatch {
            if (result.resultCode == Activity.RESULT_OK) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    onCompleteListener.onComplete(Tasks.forCanceled())
                }
            } else onCompleteListener.onComplete(Tasks.forCanceled())
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity, onCompleteListener)
    }

}