package com.auth.android.activities.signin

import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.auth.android.R
import com.auth.android.database.AuthReference.currentUser
import com.auth.android.dialogs.Alerts.ShowProgress
import com.auth.android.extensions.ComposableExtensions.noRippleClickable
import com.auth.android.ui.AppColors
import com.auth.android.ui.AuthTheme

class SignInActivity : SignInBase() {

    val LocalNavController = staticCompositionLocalOf<NavHostController> {
        error("NavController not provided")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }

    @Preview
    @Composable
    fun AppContent() {
        AuthTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SetNavController()
                if (showProgress) {
                    ShowProgress()
                }
            }
        }
    }

    @Composable
    fun SetNavController() {
        val navController = rememberNavController()
        NavigationListener(navController)
        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(navController = navController, startDestination = "signIn") {
                composable("signIn") {
                    SignInScreen()
                }
                composable("signUp") {
                    SignUpScreen()
                }
            }
        }
    }

    @Composable
    fun NavigationListener(navController: NavHostController) {
        LaunchedEffect(navController) {
            navController.currentBackStackEntryFlow.collect { backStackEntry ->
                resetMutableValues()
            }
        }
    }

    @Composable
    fun SignInScreen() {
        Column(modifier = Modifier.fillMaxSize()) {
            SignInHeader()
            SignInFields()
            EmailPasswordSignInButton()
            ORDivider()
            GoogleSignInButton()
            CreateAnAccountButtonText()
        }
    }

    @Composable
    fun SignUpScreen() {
        Column(modifier = Modifier.fillMaxSize()) {
            SignUpHeader()
            SignUpFields()
            TermsConditionsRow()
            EmailPasswordSignUpButton()
            ORDivider()
            GoogleSignInButton()
            AlreadyAccountButtonText()
        }
    }

    @Composable
    private fun SignInHeader() {
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
    private fun SignInFields() {

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(Unit) {
            rememberMe = preferences.rememberMe
            if (rememberMe) {
                email = currentUser?.email ?: ""
            }
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
                        if (isEmailValid()) {
                            forgotPassword()
                        }
                    },
                text = "Forgot password?",
                color = AppColors.blue,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }

    @Composable
    private fun SignUpHeader() {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = "Hello",
            color = AppColors.appColor,
            style = MaterialTheme.typography.displayMedium.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = "there!",
            color = AppColors.black,
            style = MaterialTheme.typography.displayMedium.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = "Create an account to access your package history and get real-time updates on all your shipments",
            color = AppColors.appText,
            style = MaterialTheme.typography.bodySmall
        )
    }

    @Composable
    private fun SignUpFields() {

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .onFocusChanged { focusState ->
                isNameFocused = focusState.isFocused
            },
            value = name,
            isError = showNameError,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (isNameFocused || name.isNotEmpty()) Icons.Rounded.Person else Icons.Outlined.Person,
                        contentDescription = "Name icon",
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Name",
                    color = if (showNameError) AppColors.error else AppColors.appText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = getOutlinedTextFieldColors(name),
            singleLine = true,
            onValueChange = {
                showNameError = false
                name = it
            })

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            visible = showNameError
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 16.dp),
                textAlign = TextAlign.End,
                text = "Please enter valid name",
                color = AppColors.error,
                style = MaterialTheme.typography.bodyMedium
            )
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
                    text = "Email",
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
                    text = "Password",
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
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
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

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .onFocusChanged { focusState ->
                isConfirmPasswordFocused = focusState.isFocused
            },
            value = confirmPassword,
            isError = showConfirmPasswordError,
            leadingIcon = {
                Row(modifier = Modifier.padding(start = 8.dp)) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (isConfirmPasswordFocused || confirmPassword.isNotEmpty()) Icons.Rounded.Lock else Icons.Outlined.Lock,
                        contentDescription = "Confirm password icon"
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Confirm password",
                    color = if (showConfirmPasswordError) AppColors.error else AppColors.appText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    val icon =
                        if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (confirmPasswordVisible) "Hide confirm password" else "Show confirm password"
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
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(48.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = getOutlinedTextFieldColors(confirmPassword),
            singleLine = true,
            onValueChange = {
                showConfirmPasswordError = false
                confirmPassword = it
            })

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            visible = showConfirmPasswordError
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .padding(end = 16.dp),
                textAlign = TextAlign.End,
                text = "Password mismatched",
                color = AppColors.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    private fun TermsConditionsRow() {

        val uriHandler = LocalUriHandler.current

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isTermsChecked,
                onCheckedChange = { isTermsChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = AppColors.appColor,
                    uncheckedColor = AppColors.unfocused,
                    checkmarkColor = AppColors.white
                )
            )

            val annotatedText = buildAnnotatedString {

                val textStart = length
                append("By signing up, you agree to our ")
                addStyle(
                    style = SpanStyle(
                        color = AppColors.appText
                    ), start = textStart, end = length
                )
                addStringAnnotation(
                    tag = "", annotation = "", start = textStart, end = length
                )

                val termsStart = length
                append("Terms of Service")
                addStyle(
                    style = SpanStyle(
                        color = AppColors.appColor,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    ), start = termsStart, end = length
                )
                addStringAnnotation(
                    tag = "terms",
                    annotation = "https://example.com/terms",
                    start = termsStart,
                    end = length
                )

                val andStart = length
                append(" and ")
                addStyle(
                    style = SpanStyle(
                        color = AppColors.appText
                    ), start = andStart, end = length
                )
                addStringAnnotation(
                    tag = "", annotation = "", start = andStart, end = length
                )

                val privacyStart = length
                append("Privacy Policy")
                addStyle(
                    style = SpanStyle(
                        color = AppColors.appColor,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    ), start = privacyStart, end = length
                )
                addStringAnnotation(
                    tag = "privacy",
                    annotation = "https://example.com/privacy",
                    start = privacyStart,
                    end = length
                )
            }

            ClickableText(
                text = annotatedText,
                style = MaterialTheme.typography.bodyMedium,
            ) { offset ->

                annotatedText.getStringAnnotations(start = offset, end = offset).firstOrNull()
                    ?.let { annotation ->
                        when (annotation.tag) {
                            "terms" -> {
                                uriHandler.openUri(annotation.item)
                            }

                            "privacy" -> {
                                uriHandler.openUri(annotation.item)
                            }

                            else -> {
                                isTermsChecked = !isTermsChecked
                            }
                        }
                    }
            }
        }
    }

    @Composable
    private fun EmailPasswordSignInButton() {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            enabled = email.trim().isNotEmpty() && password.trim().isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.appColor,
                contentColor = AppColors.white,
                disabledContainerColor = AppColors.appColor.copy(alpha = 0.5f),
                disabledContentColor = AppColors.white.copy(alpha = 0.5f)
            ),
            onClick = {
                if (isEmailValid() && isPasswordValid()) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onEmailPasswordSignIn()
                }
            }) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Sign In",
                color = AppColors.white,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    private fun EmailPasswordSignUpButton() {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            enabled = name.trim().isNotEmpty() && email.trim().isNotEmpty() && password.trim()
                .isNotEmpty() && confirmPassword.trim().isNotEmpty() && isTermsChecked,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.appColor,
                contentColor = AppColors.white,
                disabledContainerColor = AppColors.appColor.copy(alpha = 0.5f),
                disabledContentColor = AppColors.white.copy(alpha = 0.5f)
            ),
            onClick = {
                if (isEmailValid() && isPasswordValid() && isConfirmPasswordValid()) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onEmailPasswordSignUp()
                }
            }) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Sign Up",
                color = AppColors.white,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    private fun ORDivider() {
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
                googleSignIn?.signIn()
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
    private fun CreateAnAccountButtonText() {
        val navController = LocalNavController.current
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
                        navController.navigate("signUp") {
                            launchSingleTop = true
                        }
                    },
                text = "Create an account",
                color = AppColors.appColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    fun AlreadyAccountButtonText() {
        val navController = LocalNavController.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                color = AppColors.appText,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .noRippleClickable {
                        navController.popBackStack("signIn", false)
                    },
                text = "Login",
                color = AppColors.appColor,
                style = MaterialTheme.typography.bodyMedium
            )
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