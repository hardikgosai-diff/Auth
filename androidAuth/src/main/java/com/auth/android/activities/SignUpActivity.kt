package com.auth.android.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth.android.ui.AppColors
import com.auth.android.ui.AuthTheme

/**
 * 07/05/25.
 *
 * @author hardkgosai.
 */
class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            on()
        }
    }

    @Preview
    @Composable
    private fun on() {
        AuthTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppColors.white
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = "Welcome",
                    color = AppColors.appColor,
                )
            }
        }
    }

}