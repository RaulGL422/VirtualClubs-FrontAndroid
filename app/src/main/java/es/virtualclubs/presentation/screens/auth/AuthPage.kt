package es.virtualclubs.presentation.screens.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.virtualclubs.R
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.components.RoundedTextField
import es.virtualclubs.presentation.components.SocialButton
import es.virtualclubs.presentation.handlers.ErrorHandler
import es.virtualclubs.presentation.theme.getAppVersion
import es.virtualclubs.presentation.theme.getLargeLogo
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
    onSettingsTap: () -> Unit,
    onLogged: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    screenType: ScreenType
) {
    var isLogin by remember { mutableStateOf(true) }
    val uiState = viewModel.uiState.collectAsState().value
    val passwordResetUiState = viewModel.passwordResetUiState.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resetEmailSent = stringResource(R.string.password_reset_email_sent)

    LaunchedEffect(passwordResetUiState) {
        if (passwordResetUiState is PasswordResetUiState.Success) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = resetEmailSent,
                    duration = SnackbarDuration.Short
                )
                viewModel.resetPasswordRequest()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Settings button
            IconButton(
                onClick = onSettingsTap,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.End)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
            }

            // Manage activity with google
            val googleSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                viewModel.handleSignInResultGoogle(result)
            }

            // Animate transition between login and register forms
            AnimatedContent(
                targetState = isLogin,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300, delayMillis = 100)) togetherWith
                            fadeOut(animationSpec = tween(300)) using
                            SizeTransform(clip = false)
                },
                label = "auth_transition"
            ) { targetIsLogin ->
                LoginScreen(
                    targetIsLogin,
                    uiState,
                    passwordResetUiState,
                    onLogin = { email, password, rememberUser ->
                        viewModel.loginUser(email, password, rememberUser)
                    },
                    onGoogle = {
                        viewModel.beginSignInGoogle(googleSignInLauncher)
                    },
                    onFacebook = {

                    },
                    onApple = {

                    },
                    onRegister = { email, password, confirmPassword, rememberUser ->
                        viewModel.registerUser(email, password, confirmPassword, rememberUser)
                    },
                    onChangeLogin = {
                        isLogin = !isLogin
                    },
                    screenType = screenType,
                    onLogged = onLogged,
                    onForgottenPass = { email ->
                        viewModel.requestPasswordReset(email)
                    },
                )
            }
        }

    }
}

@Composable
fun LoginScreen(
    isLogin: Boolean,
    uiState: AuthUiState,
    passwordResetUiState: PasswordResetUiState,
    onLogin: (String, String, Boolean) -> Unit,
    onGoogle: () -> Unit,
    onFacebook: () -> Unit,
    onApple: () -> Unit,
    onRegister: (String, String, String, Boolean) -> Unit,
    onChangeLogin: () -> Unit,
    screenType: ScreenType,
    onLogged: () -> Unit,
    onForgottenPass: (String) -> Unit,
) {
    // Estado de los campos
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberUser by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val columnWidthFraction = when (screenType) {
        ScreenType.Small -> 0.90f
        ScreenType.Medium -> 0.65f
    }

    if (uiState is AuthUiState.Success) {
        onLogged()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(columnWidthFraction)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Column
            Image(
                painter = getLargeLogo(),
                contentDescription = stringResource(R.string.logo),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth(columnWidthFraction)
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form TextFields

            // Email TextField
            RoundedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = R.string.email_placeholder,
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            RoundedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = Icons.Default.Lock,
                placeholder = R.string.password_placeholder,
                isPassword = true,
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isLogin) {
                // Confirm Password TextField
                RoundedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    leadingIcon = Icons.Default.Lock,
                    placeholder = R.string.confirm_password_placeholder,
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remember user checkbox
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberUser,
                        onCheckedChange = { rememberUser = it }
                    )
                    Text(stringResource(R.string.remember_password))
                }
            }

            if (uiState is AuthUiState.AuthFailed) {
                Text(
                    text = stringResource(ErrorHandler.getErrorMessage(uiState.message)),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Log Button
            Button(
                enabled = uiState !is AuthUiState.AttemptingAuth,
                onClick = {
                    if (isLogin)
                        onLogin(email, password, rememberUser)
                    else
                        onRegister(email, password, password, rememberUser)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(stringResource(if (isLogin) R.string.login else R.string.register))
            }

            // Forgot Password?
            TextButton(onClick = { showForgotPasswordDialog = true }) {
                Text(stringResource(R.string.forgot_password))
            }

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
                Text("  O  ", color = Color.Gray)
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Social Buttons
            Column {
                SocialButton(R.string.continue_with_google, icon = R.drawable.google_icon) {
                    onGoogle()
                }
                Spacer(modifier = Modifier.height(4.dp))
                SocialButton(R.string.continue_with_apple, icon = R.drawable.apple_icon) {
                    onApple()
                }
                Spacer(modifier = Modifier.height(4.dp))
                SocialButton(R.string.facebook, icon = R.drawable.facebook_icon) {
                    onFacebook()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Register or login link
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(if (isLogin) R.string.dont_have_account else R.string.have_account))
                TextButton(onClick = { onChangeLogin() }) {
                    Text(stringResource(if (isLogin) R.string.register else R.string.login))
                }
            }
        }

        // App Version
        Text(
            text = getAppVersion(LocalContext.current),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 12.dp)
        )

        if (showForgotPasswordDialog) {
            ForgotPasswordDialog(
                passwordResetUiState = passwordResetUiState,
                onDismiss = {
                    showForgotPasswordDialog = false

                },
                onConfirm = { email ->
                    onForgottenPass(email)
                }
            )
        }
    }
}

@Composable
fun ForgotPasswordDialog(
    passwordResetUiState: PasswordResetUiState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    if (passwordResetUiState is PasswordResetUiState.Success) {
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.forgot_password))
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.forgot_password_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                RoundedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = R.string.email_placeholder,
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )
                if (passwordResetUiState is PasswordResetUiState.Failed)
                    Text(
                        text = stringResource(ErrorHandler.getErrorMessage(passwordResetUiState.message)),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(email) },
                enabled = email.isNotBlank() && passwordResetUiState !is PasswordResetUiState.Attempting
            ) {
                Text(stringResource(R.string.send_email))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}