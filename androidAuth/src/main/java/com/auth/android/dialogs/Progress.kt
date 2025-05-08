package com.auth.android.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.auth.android.ui.AppColors

/**
 * 08/05/25.
 *
 * @author hardkgosai.
 */
object Progress {

    @Composable
    fun ShowProgress() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.black50),
            contentAlignment = Alignment.Center
        ) {
            Card {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(16.dp),
                    color = AppColors.appColor
                )
            }
        }
    }

}