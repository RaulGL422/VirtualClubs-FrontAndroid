package es.virtualclubs.presentation.screens.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.virtualclubs.R

@Composable
fun ResetPasswordPage(
    token: String,
    onSettingsTap: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onSettingsTap,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Row {
                Image(painter = painterResource(R.drawable.logo_whitout_text), stringResource(R.string.app_name))
                Image(painter = painterResource(R.drawable.recover_password), stringResource(R.string.recover_password))
            }


        }
    }
}