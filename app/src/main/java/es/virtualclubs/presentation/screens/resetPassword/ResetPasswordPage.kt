package es.virtualclubs.presentation.screens.resetPassword

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.virtualclubs.R
import es.virtualclubs.ScreenType
import es.virtualclubs.data.managers.ErrorManager
import es.virtualclubs.presentation.components.RoundedTextField
import es.virtualclubs.presentation.handlers.ErrorHandler

@Composable
fun ResetPasswordPage(
    token: String,
    screenType: ScreenType,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    onSettingsTap: () -> Unit,
    onBack: () -> Unit,
    onPasswordResetSuccess: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val newPasswordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if (uiState is ResetPasswordUiState.Success) onPasswordResetSuccess(stringResource(R.string.password_reset_success))

    Scaffold(
        topBar = { TopBar(onSettingsTap, onBack) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(
                        when (screenType) {
                            ScreenType.Medium -> 0.7f
                            ScreenType.Small -> 0.9f
                        }
                    )
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResetPasswordHeader()

                Text(text = stringResource(R.string.introduce_reset_password))

                ResetPasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = R.string.password_placeholder,
                    imeAction = ImeAction.Next,
                    onImeAction = { confirmPasswordFocusRequester.requestFocus() },
                    modifier = Modifier.focusRequester(newPasswordFocusRequester)
                )

                ResetPasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = R.string.confirm_password_placeholder,
                    onImeAction = {
                        focusManager.clearFocus()
                        viewModel.resetPassword(token, newPassword, confirmPassword)
                    },
                    modifier = Modifier.focusRequester(confirmPasswordFocusRequester)
                )

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedVisibility(visible = ErrorManager.haveError()) {
                  Text(
                    text = stringResource(ErrorManager.getErrorId()),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                  )
                }

                Button(
                    onClick = {
                        viewModel.resetPassword(token, newPassword, confirmPassword)
                    },
                    enabled = newPassword.isNotBlank() &&
                            confirmPassword.isNotBlank() &&
                            uiState !is ResetPasswordUiState.Attempting,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.change_password))
                }
            }
        }
    }
}

@Composable
private fun TopBar(onSettingsTap: () -> Unit, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
        IconButton(onClick = onSettingsTap) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.settings)
            )
        }
    }
}

@Composable
private fun ResetPasswordHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ResetPasswordImage(
            drawable = R.drawable.logo_whitout_text,
            description = R.string.app_name
        )
        ResetPasswordImage(
            drawable = R.drawable.recover_password,
            description = R.string.recover_password
        )
    }
}

@Composable
private fun ResetPasswordImage(
    @DrawableRes drawable: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(drawable),
        contentDescription = stringResource(description),
        modifier = modifier
            .fillMaxWidth(0.35f)
            .aspectRatio(1f)
    )
}

@Composable
private fun ResetPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    RoundedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = Icons.Default.Lock,
        placeholder = placeholder,
        isPassword = true,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        onImeAction = onImeAction,
        modifier = modifier
    )
}