package es.virtualclubs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import es.virtualclubs.R
import es.virtualclubs.data.managers.GlobalUIManager

@Composable
fun VCScaffold(
  topBar: (@Composable () -> Unit)? = null,
  enableTopBar: Boolean = true,
  titleTopBar: Int? = null,
  stringTopBar: String? = null,
  topBarActions: (@Composable RowScope.() -> Unit)? = null,
  canGoBack: Boolean = false,
  onNavigateBack: (() -> Unit)? = null,
  modifier: Modifier = Modifier,
  snackbarHost: @Composable () -> Unit = {},
  containerColor: Color = MaterialTheme.colorScheme.background,
  contentColor: Color = contentColorFor(containerColor),
  contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
  content: @Composable (PaddingValues) -> Unit
) {
  val loading by GlobalUIManager.isLoading.collectAsState()
  val dialog by GlobalUIManager.dialogState.collectAsState()

  Box {
    Scaffold(
      topBar = {
        if (enableTopBar) {
          if (topBar != null) {
            topBar()
          } else {
            AppBar(
              canGoBack = canGoBack,
              titleResId = titleTopBar ?: R.string.app_name,
              titleString = stringTopBar,
              onNavigateBack = onNavigateBack,
              actions = topBarActions,
            )
          }
        }
      },
      snackbarHost = snackbarHost,
      containerColor = containerColor,
      contentColor = contentColor,
      contentWindowInsets = contentWindowInsets,
      modifier = modifier
    ) {
      if (loading) {
        Box(
          Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f)),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator()
        }
      } else {
        content(it)
      }

      if (dialog.visible) {
        AlertDialog(
          onDismissRequest = {
            if (!dialog.blockDialog) {
              GlobalUIManager.hideDialog()
            }
          },
          title = { dialog.title?.let { text -> Text(stringResource(text)) } },
          text = { dialog.content },
          confirmButton = {
            if (!dialog.blockDialog) {
              dialog.onConfirm?.let { onConfirm ->
                TextButton(onClick = {
                  onConfirm()
                  GlobalUIManager.hideDialog()
                }) {
                  Text(stringResource(dialog.confirmText ?: R.string.confirm))
                }
              }
            }
          },
          properties = DialogProperties(
            dismissOnBackPress = dialog.dismissible,
            dismissOnClickOutside = dialog.dismissible
          )
        )
      }
    }
  }
}