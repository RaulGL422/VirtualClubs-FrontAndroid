/**
 * Row at the bottom of the auth form that switches between login and register modes.
 * Shows "¿No tienes cuenta? Registrarse" or "¿Ya tienes cuenta? Iniciar Sesión".
 */
package es.virtualclubs.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.virtualclubs.R
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle

@Composable
internal fun AuthToggle(isLogin: Boolean, onChangeLogin: () -> Unit) {
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
