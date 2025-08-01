package es.virtualclubs.presentation.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import es.virtualclubs.R
import es.virtualclubs.presentation.components.RoundedTextField
import es.virtualclubs.presentation.components.SocialButton

@Composable
fun LoginPage(
    onSettingsTap: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var isLogin by remember { mutableStateOf(true) }
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val activity = context as Activity

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Settings button
            IconButton(
                onClick = onSettingsTap,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
            }

            // Manage activity with google
            val googleSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.handleSignInResultGoogle(result.data)
                } else {
                    viewModel.onGoogleLoginFailed("Resultado cancelado o erróneo")
                }
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
                    onLogin = { email, password ->
                        viewModel.loginUser(email, password)
                    },
                    onGoogle = {
                        viewModel.beginSignInGoogle(activity, googleSignInLauncher)
                    },
                    onFacebook = {

                    },
                    onApple = {

                    },
                    onRegister = { email, password, confirmPassword ->

                    },
                    onChangeLogin = {
                        isLogin = !isLogin
                    }
                )
            }
        }

    }
}

@Composable
fun LoginScreen(
    isLogin: Boolean,
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onGoogle: () -> Unit,
    onFacebook: () -> Unit,
    onApple: () -> Unit,
    onRegister: (String, String, String) -> Unit,
    onChangeLogin: () -> Unit
) {
    // Estado de los campos
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberUser by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo (puede ser Image)
            Icon(
                painter = painterResource(id = R.drawable.logo_whitout_text),
                contentDescription = "Logo",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF1E88E5) // Azul principal
            )
            Text(
                text = "VirtualClubs",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            )
            Text(
                text = "Gestiona, descubre y disfruta del deporte",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de texto
        Column {
            RoundedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = R.string.email_placeholder,
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            RoundedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardType = KeyboardType.Password
            )

            if (!isLogin) {
                RoundedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberUser,
                    onCheckedChange = { rememberUser = it }
                )
                Text("Recordar usuario")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón iniciar sesión
        Button(
            onClick = { if (isLogin) onLogin(email, password) else onRegister(email, password, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ¿Has olvidado tu contraseña?
        TextButton(onClick = { showForgotPasswordDialog = true }) {
            Text("¿Has olvidado tu contraseña?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Divider con "O"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f))
            Text("  O  ", color = Color.Gray)
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botones sociales
        Column {
            SocialButton(text = "Continuar con Google", icon = R.drawable.google_icon) {
                onGoogle()
            }
            Spacer(modifier = Modifier.height(4.dp))
            SocialButton(text = "Continuar con Apple", icon = R.drawable.apple_icon) {
                onApple()
            }
            Spacer(modifier = Modifier.height(4.dp))
            SocialButton(text = "Continuar con Facebook", icon = R.drawable.facebook_icon) {
                onFacebook()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ¿No tienes cuenta? Regístrate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("¿No tienes cuenta? ")
            TextButton(onClick = { onChangeLogin() }) {
                Text("Regístrate")
            }
        }

        // Versión de la app
        Text(
            text = "v1.0",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (showForgotPasswordDialog) {
            var recoveryEmail by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showForgotPasswordDialog = false },
                title = { Text("Recuperar contraseña") },
                text = {
                    Column {
                        Text("Introduce tu correo electrónico y te enviaremos un código para cambiar tu contraseña.")
                        Spacer(modifier = Modifier.height(8.dp))
                        RoundedTextField(
                            value = recoveryEmail,
                            onValueChange = { recoveryEmail = it },
                            placeholder = R.string.email_placeholder,
                            leadingIcon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        // TODO: Acción de recuperación de contraseña
                        showForgotPasswordDialog = false
                    }) {
                        Text("Solicitar código")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showForgotPasswordDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}