package com.auth.android.activities

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.auth.android.R
import com.auth.android.base.BaseActivity
import com.auth.android.dialogs.Progress.ShowProgress
import com.auth.android.extensions.ComposableExtensions.noRippleClickable
import com.auth.android.ui.AppColors
import com.auth.android.ui.AuthTheme
import com.auth.android.utils.GoogleSignIn

class SignInActivity : BaseActivity() {

    private var showProgress by mutableStateOf(false)
    private var googleSignIn: GoogleSignIn? = null

    override fun onCreate() {
        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        googleSignIn = GoogleSignIn(this) { authResult ->
            showProgress = false
            if (authResult.isSuccessful) {

            } else {

            }
        }
    }

    @Preview(showBackground = true, backgroundColor = AppColors.whiteHexFormat)
    @Composable
    override fun OnSurfaceCreated() {
        AuthTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
            ) {
                TopHeader()
                ContentView()
            }

            if (showProgress) {
                googleSignIn?.signIn()
                ShowProgress()
            }
        }
    }

    @Composable
    private fun TopHeader() {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = "Welcome",
            color = AppColors.appColor,
            style = MaterialTheme.typography.displayMedium.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "back!",
            color = AppColors.black,
            style = MaterialTheme.typography.displayMedium.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = "Sign In to access your package history and get real-time updates on all your shipments",
            color = AppColors.appText,
            style = MaterialTheme.typography.bodySmall
        )
    }

    @Composable
    private fun ContentView() {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var showEmailError by remember { mutableStateOf(false) }
        var showPasswordError by remember { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }
        var rememberMe by remember { mutableStateOf(false) }
        var isEmailFocused by remember { mutableStateOf(false) }
        var isPasswordFocused by remember { mutableStateOf(false) }

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(Unit) {
            rememberMe = preferences.rememberMe
            if (rememberMe) {
                email = preferences.user.safeEmail
            }
        }

        fun onSignIn() {
            showEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (showEmailError) return

            showPasswordError = !Regex(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#^+=._-]).{6,}$"
            ).matches(password)

            if (showPasswordError) return

            focusManager.clearFocus()
            keyboardController?.hide()

            preferences.rememberMe = rememberMe
        }

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .onFocusChanged { focusState ->
                isEmailFocused = focusState.isFocused
            },
            value = email,
            isError = showEmailError,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (isEmailFocused || email.isNotEmpty()) Icons.Rounded.Email else Icons.Outlined.Email,
                        contentDescription = "Email icon",
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Enter email",
                    color = if (showEmailError) AppColors.error else AppColors.appText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = getOutlinedTextFieldColors(email),
            singleLine = true,
            onValueChange = {
                showEmailError = false
                email = it
            })

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            visible = showEmailError
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 16.dp),
                textAlign = TextAlign.End,
                text = "Please enter valid email",
                color = AppColors.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .onFocusChanged { focusState ->
                isPasswordFocused = focusState.isFocused
            },
            value = password,
            isError = showPasswordError,
            leadingIcon = {
                Row(modifier = Modifier.padding(start = 8.dp)) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (isPasswordFocused || password.isNotEmpty()) Icons.Rounded.Lock else Icons.Outlined.Lock,
                        contentDescription = "Password icon"
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Enter password",
                    color = if (showPasswordError) AppColors.error else AppColors.appText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    val icon =
                        if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = getOutlinedTextFieldColors(password),
            singleLine = true,
            onValueChange = {
                showPasswordError = false
                password = it
            })

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            visible = showPasswordError
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 16.dp),
                textAlign = TextAlign.End,
                text = "Please enter valid password",
                color = AppColors.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppColors.appColor,
                        uncheckedColor = AppColors.unfocused,
                        checkmarkColor = AppColors.white
                    )
                )
                Text(
                    modifier = Modifier.noRippleClickable { rememberMe = !rememberMe },
                    text = "Remember me",
                    color = AppColors.appText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .noRippleClickable {

                    },
                text = "Forgot password?",
                color = AppColors.blue,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.appColor,
                contentColor = AppColors.white,
                disabledContainerColor = AppColors.appColor.copy(alpha = 0.5f),
                disabledContentColor = AppColors.white.copy(alpha = 0.5f)
            ),
            onClick = { onSignIn() }) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Sign In",
                color = AppColors.white,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp), color = AppColors.unfocused
            )

            Text(
                text = "Or",
                color = AppColors.appText,
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp), color = AppColors.unfocused
            )
        }

        GoogleSignInButton()

        /*OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = AppColors.white, contentColor = AppColors.black
            ),
            border = BorderStroke(1.dp, AppColors.unfocused),
            onClick = {
                viewModel.showProgress = true
            }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google icon"
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Continue with Google",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }*/

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account?",
                color = AppColors.appText,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .noRippleClickable {

                    },
                text = "Create an account",
                color = AppColors.appColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    private fun GoogleSignInButton() {
        OutlinedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = AppColors.white, contentColor = AppColors.black
            ),
            border = BorderStroke(1.dp, AppColors.unfocused),
            onClick = {
                showProgress = true
            }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google icon"
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Continue with Google",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    @Composable
    private fun getOutlinedTextFieldColors(string: String) = OutlinedTextFieldDefaults.colors(
        cursorColor = AppColors.black,
        errorCursorColor = AppColors.error,

        focusedBorderColor = AppColors.focused,
        unfocusedBorderColor = if (string.isEmpty()) AppColors.unfocused else AppColors.focused,
        errorBorderColor = AppColors.error,

        focusedTextColor = AppColors.black,
        unfocusedTextColor = if (string.isEmpty()) AppColors.unfocused else AppColors.focused,
        errorTextColor = AppColors.error,

        focusedLeadingIconColor = AppColors.focused,
        unfocusedLeadingIconColor = if (string.isEmpty()) AppColors.unfocused else AppColors.focused,
        errorLeadingIconColor = AppColors.error,

        focusedTrailingIconColor = AppColors.focused,
        unfocusedTrailingIconColor = if (string.isEmpty()) AppColors.unfocused else AppColors.focused,
        errorTrailingIconColor = if (string.isEmpty()) AppColors.unfocused else AppColors.focused,
    )

}