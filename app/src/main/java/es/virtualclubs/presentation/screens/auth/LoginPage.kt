package es.iesfernandoaguilar.ui.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.iesfernandoaguilar.R
import es.virtualclubs.models.objects.ErrorHandler
import es.virtualclubs.presentation.components.RoundedTextField
import es.iesfernandoaguilar.ui.navigation.Destination
import es.iesfernandoaguilar.ui.viewmodel.LoginState
import es.iesfernandoaguilar.ui.viewmodel.LoginViewModel

object LoginDestination : Destination {
    override val route = "login"
    override val titleRes = R.string.loginpage
}

@Composable
fun LoginPage(
    onSettingsTap: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var isLogin by remember { mutableStateOf(true) }
    val state = viewModel.state.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSettingsTap,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings_page),
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.outline,
                            MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
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
                LoginRegisterCard(
                    isLogin = targetIsLogin,
                    state = state,
                    viewModel = viewModel,
                    onToggle = { isLogin = !isLogin },
                    onActionClick = { ->
                        if (targetIsLogin) {
                            // Call login function on ViewModel
                            viewModel.loginUser()
                        } else {
                            // Call register function on ViewModel
                            viewModel.registerUser()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LoginRegisterCard(
    isLogin: Boolean,
    onToggle: () -> Unit,
    state: LoginState,
    viewModel: LoginViewModel,
    onActionClick: () -> Unit
) {
    // Get current error info if any
    val errorMessage = ErrorHandler.errorMessage
    val errorAction = ErrorHandler.errorAction
    val errorActionName = ErrorHandler.errorActionName

    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title text, changes depending on login or register mode
            Text(
                text = stringResource(if (isLogin) R.string.welcome_again else R.string.register),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Username field only for registration
            if (!isLogin) {
                RoundedTextField(
                    value = state.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    placeholder = R.string.username,
                    leadingIcon = Icons.Default.Person
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Email field for both login and register
            RoundedTextField(
                value = state.email,
                onValueChange = { viewModel.updateEmail(it) },
                placeholder = R.string.email,
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field for both login and register
            RoundedTextField(
                value = state.password,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = R.string.password,
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm password field only for registration
            if (!isLogin) {
                RoundedTextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.updateConfirmPassword(it) },
                    placeholder = R.string.confirm_password,
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Checkbox for keeping session (auto-login)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .toggleable(
                        value = state.checkedAutologinBox,
                        onValueChange = { viewModel.updateCheckedAutologinBox(it) },
                        role = Role.Checkbox
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.checkedAutologinBox,
                    onCheckedChange = null
                )
                Text(
                    text = stringResource(R.string.keep_session),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Button to trigger login or registration action
            Button(
                onClick = { onActionClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = stringResource(if (isLogin) R.string.login else R.string.register))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show error message and optional action if any
            if (errorMessage != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = errorMessage),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (errorAction != null && errorActionName != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = errorActionName),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.clickable { errorAction() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Toggle text to switch between login and register
            TextButton(onClick = onToggle) {
                Text(
                    text = stringResource(
                        if (isLogin) R.string.dont_have_account else R.string.already_member
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun LoginScreen() {
    // Estado de los campos
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberUser by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo + título + subtítulo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo (puede ser Image)
            Icon(
                painter = painterResource(id = R.drawable.ic_virtualclubs_logo),
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
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
            onClick = { /* TODO: Acción de login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ¿Has olvidado tu contraseña?
        TextButton(onClick = { /* TODO: Acción */ }) {
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
            SocialLoginButton(text = "Continuar con Google", icon = R.drawable.ic_google) {
                // TODO: Acción Google
            }
            Spacer(modifier = Modifier.height(4.dp))
            SocialLoginButton(text = "Continuar con Apple", icon = R.drawable.ic_apple) {
                // TODO: Acción Apple
            }
            Spacer(modifier = Modifier.height(4.dp))
            SocialLoginButton(text = "Continuar con Facebook", icon = R.drawable.ic_facebook) {
                // TODO: Acción Facebook
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ¿No tienes cuenta? Regístrate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("¿No tienes cuenta? ")
            TextButton(onClick = { /* TODO: Navegar a registro */ }) {
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
    }
}

@Composable
fun SocialLoginButton(text: String, icon: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}