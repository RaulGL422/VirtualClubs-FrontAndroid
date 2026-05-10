package es.virtualclubs.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.virtualclubs.R
import es.virtualclubs.presentation.managers.LocalGlobalUIManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
  canGoBack: Boolean,
  @StringRes titleResId: Int,
  modifier: Modifier = Modifier,
  titleString: String? = null,
  onNavigateBack: (() -> Unit)? = null,
  actions: (@Composable RowScope.() -> Unit)? = null
) {
  val globalUIManager = LocalGlobalUIManager.current
  val errorState by globalUIManager.errorState.collectAsStateWithLifecycle()

  val message = when (errorState.code) {
    null -> UiMessage.None
    else -> UiMessage.Error(
      messageKey = globalUIManager.getErrorId()
    )
  }

  Column(modifier = modifier) {

    TopAppBar(
      title = { Text(text = titleString ?: stringResource(id = titleResId)) },
      navigationIcon = {
        if (canGoBack && onNavigateBack != null) {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        }
      },
      actions = { actions?.invoke(this) },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor        = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
      )
    )

    val messageId = when (message) {
      is UiMessage.None -> null
      is UiMessage.Error -> message.messageKey
    }

    if (messageId != null) {
      Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(id = messageId),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onErrorContainer
          )

          IconButton(onClick = {
            globalUIManager.clearError()
          }) {
            Icon(Icons.Default.Close, contentDescription = stringResource(id = R.string.close))
          }
        }
      }
    }
  }
}

sealed class UiMessage {
  data class Error(val messageKey: Int) : UiMessage()
  object None : UiMessage()
}