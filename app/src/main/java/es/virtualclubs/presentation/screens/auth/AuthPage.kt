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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
    message: String? = null,
    screenType: ScreenType
) {
    var isLogin by remember { mutableStateOf(true) }
    val uiState by viewModel.uiState.collectAsState()
    val passwordResetUiState by viewModel.passwordResetUiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resetEmailSent = stringResource(R.string.password_reset_email_sent)

    if (uiState is AuthUiState.Success) onLogged()

    // Handle password reset snackbar
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

    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { AuthTopBar(onSettingsTap) }
    ) { padding ->
        // Google Sign In launcher
        val googleSignInLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result -> viewModel.handleSignInResultGoogle(result) }
        )

        AnimatedContent(
            targetState = isLogin,
            transitionSpec = {
                fadeIn(animationSpec = tween(300, delayMillis = 100)) togetherWith
                        fadeOut(animationSpec = tween(300)) using
                        SizeTransform(clip = false)
            },
            label = "auth_transition"
        ) { targetIsLogin ->
            LoginContent(
                isLogin = targetIsLogin,
                uiState = uiState,
                passwordResetUiState = passwordResetUiState,
                screenType = screenType,
                onLogin = { email, pass, remember -> viewModel.loginUser(email, pass, remember) },
                onRegister = { email, pass, confirm, remember -> viewModel.registerUser(email, pass, confirm, remember) },
                onGoogle = { viewModel.beginSignInGoogle(googleSignInLauncher) },
                onFacebook = { /* TODO */ },
                onApple = { /* TODO */ },
                onChangeLogin = { isLogin = !isLogin },
                onForgottenPass = { email -> viewModel.requestPasswordReset(email) },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun AuthTopBar(onSettingsTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onSettingsTap) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.settings)
            )
        }
    }
}

@Composable
fun LoginContent(
    isLogin: Boolean,
    uiState: AuthUiState,
    passwordResetUiState: PasswordResetUiState,
    screenType: ScreenType,
    onLogin: (String, String, Boolean) -> Unit,
    onRegister: (String, String, String, Boolean) -> Unit,
    onGoogle: () -> Unit,
    onFacebook: () -> Unit,
    onApple: () -> Unit,
    onChangeLogin: () -> Unit,
    onForgottenPass: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Local states
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var rememberUser by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val columnWidthFraction = when (screenType) {
        ScreenType.Small -> 0.90f
        ScreenType.Medium -> 0.65f
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(columnWidthFraction)
                .wrapContentHeight()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = getLargeLogo(),
                contentDescription = stringResource(R.string.logo),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(columnWidthFraction)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            RoundedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = R.string.email_placeholder,
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = {
                    passwordFocusRequester.requestFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            RoundedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = R.string.password_placeholder,
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardType = KeyboardType.Password,
                imeAction = if (isLogin) ImeAction.Done else ImeAction.Next,
                onImeAction = {
                    if (isLogin) onLogin(email, password, rememberUser)
                    else confirmPasswordFocusRequester.requestFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester)
                    .padding(horizontal = 16.dp)
            )

            if (!isLogin) {
                Spacer(modifier = Modifier.height(16.dp))
                RoundedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = R.string.confirm_password_placeholder,
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        focusManager.clearFocus()
                        onRegister(email, password, confirmPassword, rememberUser)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(confirmPasswordFocusRequester)
                        .padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remember user checkbox
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberUser,
                        onCheckedChange = { rememberUser = it }
                    )
                    Text(stringResource(R.string.remember_password))
                }
            }

            when (uiState) {
                is AuthUiState.AuthFailed -> Text(
                    text = stringResource(ErrorHandler.getErrorMessage(uiState.errorType)),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                else -> Unit
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Login/Register button
            Button(
                onClick = {
                    if (isLogin) onLogin(email, password, rememberUser)
                    else onRegister(email, password, confirmPassword, rememberUser)
                },
                enabled = uiState !is AuthUiState.AttemptingAuth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(if (isLogin) R.string.login else R.string.register))
            }

            // Forgot password
            TextButton(onClick = { showForgotPasswordDialog = true }) {
                Text(stringResource(R.string.forgot_password))
            }

            // Divider
            AuthDivider()

            Spacer(modifier = Modifier.height(8.dp))

            // Social buttons
            SocialButton(R.string.continue_with_google, R.drawable.google_icon, onGoogle)
            Spacer(modifier = Modifier.height(4.dp))
            SocialButton(R.string.continue_with_apple, R.drawable.apple_icon, onApple)
            Spacer(modifier = Modifier.height(4.dp))
            SocialButton(R.string.facebook, R.drawable.facebook_icon, onFacebook)

            Spacer(modifier = Modifier.height(8.dp))

            // Register/Login toggle
            AuthToggle(isLogin, onChangeLogin)
        }

        // App version bottom
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
                onDismiss = { showForgotPasswordDialog = false },
                onConfirm = onForgottenPass
            )
        }
    }
}

@Composable
private fun AuthDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(Modifier.weight(1f))
        Text("  O  ", color = Color.Gray)
        HorizontalDivider(Modifier.weight(1f))
    }
}

@Composable
private fun AuthToggle(isLogin: Boolean, onChangeLogin: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(if (isLogin) R.string.dont_have_account else R.string.have_account))
        TextButton(onClick = onChangeLogin) {
            Text(stringResource(if (isLogin) R.string.register else R.string.login))
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

    if (passwordResetUiState is PasswordResetUiState.Success) onDismiss()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.forgot_password)) },
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
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordResetUiState is PasswordResetUiState.Failed) {
                    Text(
                        text = stringResource(ErrorHandler.getErrorMessage(passwordResetUiState.errorType)),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(email) },
                enabled = email.isNotBlank() && passwordResetUiState !is PasswordResetUiState.Attempting
            ) { Text(stringResource(R.string.send_email)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}