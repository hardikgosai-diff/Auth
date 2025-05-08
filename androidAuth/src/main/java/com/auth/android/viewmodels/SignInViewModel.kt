package com.auth.android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * 08/05/25.
 *
 * @author hardkgosai.
 */
class SignInViewModel() : ViewModel() {

    var showProgress by mutableStateOf(false)

}