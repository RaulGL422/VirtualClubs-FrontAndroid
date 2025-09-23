package es.virtualclubs.presentation.screens.resetPassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ResetPasswordPage(token: String) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Reset Password Page with Token: $token")
        }
    }
}