package com.auth.android.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.auth.android.ui.AppColors
import com.auth.android.ui.Preferences

/**
 * 02/05/25.
 *
 * @author hardkgosai.
 */
abstract class BaseActivity : ComponentActivity() {

    val preferences by lazy { Preferences(this) }

    abstract fun onCreate()

    @Composable
    abstract fun OnSurfaceCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onCreate()
        setContent { AppContent() }
    }

    @Composable
    fun AppContent() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppColors.white
        ) {
            OnSurfaceCreated()
        }
    }

}