package com.auth.android.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.auth.android.ui.AppColors

/**
 * 08/05/25.
 *
 * @author hardkgosai.
 */
object Alerts {

    @Composable
    fun ShowProgress(
        dismissOnClickOutside: Boolean = false,
        dismissOnBackPress: Boolean = false,
        onDismissRequest: () -> Unit = {}
    ) {
        val properties = DialogProperties(
            dismissOnClickOutside = dismissOnClickOutside,
            dismissOnBackPress = dismissOnBackPress
        )
        Dialog(onDismissRequest = onDismissRequest, properties = properties) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(AppColors.white, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AppColors.appColor,
                    strokeWidth = 3.dp
                )
            }
        }
    }

}