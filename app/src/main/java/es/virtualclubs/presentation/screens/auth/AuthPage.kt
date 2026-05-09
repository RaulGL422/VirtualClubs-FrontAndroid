/**
 * Auth screen entry point.
 *
 * [LoginPage] — root composable wired to [AuthViewModel], handles snackbars and navigation.
 * [LoginContent] — stateless layout: form fields, social buttons, settings icon and version label.
 * Dialogs and sub-composables are split into dialogs/ and components/ within this package.
 */
package es.virtualclubs.presentation.screens.auth

import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import es.virtualclubs.presentation.managers.LocalGlobalUIManager
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
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val passwordResetUiState by viewModel.passwordResetUiState.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val resetEmailSent = stringResource(R.string.password_reset_email_sent)

  LaunchedEffect(uiState) {
    if (uiState is AuthUiState.Success) onLogged()
  }

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

  val activity = LocalActivity.current

  VCScaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    enableTopBar = false
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
        onSettingsTap = onSettingsTap,
        onLogin = { email, pass -> viewModel.loginUser(email, pass) },
        onRegister = { email, pass, confirm ->
          viewModel.registerUser(email, pass, confirm)
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
fun LoginContent(
  isLogin: Boolean,
  uiState: AuthUiState,
  passwordResetUiState: PasswordResetUiState,
  screenType: ScreenType,
  onSettingsTap: () -> Unit,
  onLogin: (String, String) -> Unit,
  onRegister: (String, String, String) -> Unit,
  onGoogle: () -> Unit,
  onFacebook: () -> Unit,
  onApple: () -> Unit,
  onChangeLogin: () -> Unit,
  onForgottenPass: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var showForgotPasswordDialog by remember { mutableStateOf(false) }
  val emailFocusRequester = remember { FocusRequester() }
  val passwordFocusRequester = remember { FocusRequester() }
  val confirmPasswordFocusRequester = remember { FocusRequester() }
  val focusManager = LocalFocusManager.current
  val globalUIManager = LocalGlobalUIManager.current

  val columnWidthFraction = when (screenType) {
    ScreenType.Small -> 0.85f
    ScreenType.Medium -> 0.45f
  }

  Box(modifier = modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth(columnWidthFraction)
          .padding(vertical = VCTheme.spacing.screenPaddingTriple),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Image(
          painter = getLargeLogo(),
          contentDescription = stringResource(R.string.logo),
          contentScale = ContentScale.FillWidth,
          modifier = Modifier
            .fillMaxWidth()
            .padding(VCTheme.spacing.componentPaddingLg)
        )

        Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))

        RoundedTextField(
          value = email,
          onValueChange = { email = it },
          placeholder = R.string.email_placeholder,
          leadingIcon = Icons.Default.Email,
          keyboardType = KeyboardType.Email,
          imeAction = ImeAction.Next,
          onImeAction = { passwordFocusRequester.requestFocus() },
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(emailFocusRequester)
        )

        Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))

        RoundedTextField(
          value = password,
          onValueChange = { password = it },
          placeholder = R.string.password_placeholder,
          leadingIcon = Icons.Default.Lock,
          isPassword = true,
          keyboardType = KeyboardType.Password,
          imeAction = if (isLogin) ImeAction.Done else ImeAction.Next,
          onImeAction = {
            if (isLogin) onLogin(email, password)
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
              onRegister(email, password, confirmPassword)
            },
            modifier = Modifier
              .fillMaxWidth()
              .focusRequester(confirmPasswordFocusRequester)
          )
        }

        Spacer(modifier = Modifier.height(VCTheme.spacing.sectionSpacingCompact))

        if (globalUIManager.haveError()) {
          Text(
            text = stringResource(globalUIManager.getErrorId()),
            color = VCTheme.colors.error
          )
          Spacer(modifier = Modifier.height(VCTheme.spacing.itemSpacingExpanded))
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Start
        ) {
          VCButton(
            content = VCButtonContent.Text(R.string.forgot_password),
            style = VCButtonStyle.Text,
            onClick = { showForgotPasswordDialog = true }
          )
        }

        Spacer(modifier = Modifier.height(VCTheme.spacing.itemSpacingCompact))

        VCButton(
          content = VCButtonContent.Text(if (isLogin) R.string.login else R.string.register),
          enabled = uiState !is AuthUiState.AttemptingAuth,
          onClick = {
            if (isLogin) onLogin(email, password)
            else onRegister(email, password, confirmPassword)
          },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(VCTheme.spacing.itemVerticalPadding))

        AuthDivider()

        Spacer(modifier = Modifier.height(VCTheme.spacing.itemVerticalPadding))

        val onSurface = MaterialTheme.colorScheme.onSurface
        if (screenType == ScreenType.Small) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(VCTheme.spacing.buttonPaddingVertical)
          ) {
            SocialIconButton(R.drawable.google_icon, R.string.continue_with_google, iconTint = null, onGoogle, Modifier.weight(1f))
            SocialIconButton(R.drawable.apple_icon, R.string.continue_with_apple, iconTint = onSurface, onApple, Modifier.weight(1f))
            SocialIconButton(R.drawable.facebook_icon, R.string.facebook, iconTint = FacebookBlue, onFacebook, Modifier.weight(1f))
          }
        } else {
          SocialButton(R.string.continue_with_google, R.drawable.google_icon, iconTint = null, onGoogle)
          Spacer(modifier = Modifier.height(VCTheme.spacing.buttonPaddingVertical))
          SocialButton(R.string.continue_with_apple, R.drawable.apple_icon, iconTint = onSurface, onApple)
          Spacer(modifier = Modifier.height(VCTheme.spacing.buttonPaddingVertical))
          SocialButton(R.string.facebook, R.drawable.facebook_icon, iconTint = FacebookBlue, onFacebook)
        }

        Spacer(modifier = Modifier.height(VCTheme.spacing.buttonPaddingVerticalExpanded))

        AuthToggle(isLogin, onChangeLogin)
      }
    }

    VCButton(
      content = VCButtonContent.Icon(VCIcon.Vector(Icons.Filled.Settings)),
      style = VCButtonStyle.Icon,
      iconSize = VCTheme.sizes.iconMd,
      onClick = onSettingsTap,
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(
          top = VCTheme.spacing.screenPaddingDouble,
          end = VCTheme.spacing.screenHorizontal
        )
    )

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
