package es.virtualclubs.presentation.screens.auth

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import es.virtualclubs.presentation.managers.GlobalUIManager
import es.virtualclubs.presentation.components.RoundedTextField
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle
import es.virtualclubs.presentation.components.VCIcon
import es.virtualclubs.presentation.components.VCScaffold
import es.virtualclubs.presentation.theme.VCTheme
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

  val activity = LocalContext.current as? Activity

  VCScaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = { AuthTopBar(onSettingsTap) }
  ) { padding ->
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
        onRegister = { email, pass, confirm, remember ->
          viewModel.registerUser(
            email,
            pass,
            confirm,
            remember
          )
        },
        onGoogle = { activity?.let { viewModel.beginSignInGoogle(it) } },
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
      .padding(
        vertical = VCTheme.spacing.screenPaddingDouble,
        horizontal = VCTheme.spacing.screenHorizontal
      ),
    horizontalArrangement = Arrangement.End
  ) {
    VCButton(
      content = VCButtonContent.Icon(VCIcon.Vector(Icons.Filled.Settings)),
      style = VCButtonStyle.Icon,
      iconSize = VCTheme.sizes.iconMd,
      onClick = onSettingsTap
    )
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
        .padding(VCTheme.spacing.sectionSpacingCompact)
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

      Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingExpanded))

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
      )

      Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))

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
      )

      if (!isLogin) {
        Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))
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
        )
      }

      Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))

      // Remember user checkbox
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = VCTheme.spacing.itemHorizontalPadding)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Checkbox(
            checked = rememberUser,
            onCheckedChange = { rememberUser = it }
          )
          Text(stringResource(R.string.remember_password))
        }
      }

      if (GlobalUIManager.haveError()) {
        Text(
          text = stringResource(GlobalUIManager.getErrorId()),
          color = VCTheme.colors.error
        )
      }

      Spacer(modifier = Modifier.height(VCTheme.spacing.itemSpacingExpanded))

      // Login/Register button
      VCButton(
        content = VCButtonContent.Text(if (isLogin) R.string.login else R.string.register),
        enabled = uiState !is AuthUiState.AttemptingAuth,
        onClick = {
          if (isLogin) onLogin(email, password, rememberUser)
          else onRegister(email, password, confirmPassword, rememberUser)
        },
        modifier = Modifier
          .fillMaxWidth()
      )

      // Forgot password
      VCButton(
        content = VCButtonContent.Text(R.string.forgot_password),
        style = VCButtonStyle.Text,
        onClick = { showForgotPasswordDialog = true }
      )

      // Divider
      AuthDivider()

      Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))

      // Social buttons
      _SocialButton(R.string.continue_with_google, R.drawable.google_icon, onGoogle)
      Spacer(modifier = Modifier.height(VCTheme.spacing.buttonPaddingVertical))
      _SocialButton(R.string.continue_with_apple, R.drawable.apple_icon, onApple)
      Spacer(modifier = Modifier.height(VCTheme.spacing.buttonPaddingVertical))
      _SocialButton(R.string.facebook, R.drawable.facebook_icon, onFacebook)

      Spacer(modifier = Modifier.height(VCTheme.spacing.buttonPaddingVerticalExpanded))

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
    VCButton(
      content = VCButtonContent.Text(if (isLogin) R.string.register else R.string.login),
      style = VCButtonStyle.Text,
      onClick = onChangeLogin
    )
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
        if (GlobalUIManager.haveError()) {
          Text(
            text = stringResource(GlobalUIManager.getErrorId()),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(vertical = 8.dp)
          )
        }
      }
    },
    confirmButton = {
      VCButton(
        content = VCButtonContent.Text(R.string.send_email),
        enabled = email.isNotBlank() && passwordResetUiState !is PasswordResetUiState.Attempting,
        shape = VCTheme.shapes.large,
        onClick = { onConfirm(email) }
      )
    },
    dismissButton = {
      VCButton(
        content = VCButtonContent.Text(R.string.close),
        style = VCButtonStyle.Text,
        onClick = onDismiss
      )
    }
  )
}

@Composable
fun _SocialButton(@StringRes text: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
  VCButton(
    content = VCButtonContent.TextAndIcon(
      text = text, VCIcon.Drawable(icon)
    ),
    style = VCButtonStyle.Outline,
    onClick = onClick,
    modifier = Modifier.fillMaxWidth()
  )
}