package com.auth.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth.android.R

@Composable
fun AuthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val plusJakartaSans = FontFamily(
        Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
        Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
        Font(R.font.plus_jakarta_sans_semi_bold, FontWeight.SemiBold),
        Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold)
    )

    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF3700B3)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5),
            tertiary = Color(0xFF3700B3)
        )
    }

    val typography = Typography(
        displayLarge = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp
        ),
        displayMedium = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        titleLarge = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        labelLarge = TextStyle(
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    )

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
