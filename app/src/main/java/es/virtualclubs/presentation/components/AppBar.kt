package es.virtualclubs.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import es.virtualclubs.models.objects.ErrorHandler
import es.virtualclubs.models.objects.NotifyHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    canGoBack: Boolean,
    @StringRes titleResId: Int,
    titleString: String? = null,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    val errorMessageId = ErrorHandler.errorMessage
    val errorAction = ErrorHandler.errorAction
    val errorActionNameId = ErrorHandler.errorActionName
    val notifyMessageId = NotifyHandler.notifyMessage

    Column(modifier = modifier) {

        // Top bar with title and optional navigation & actions
        TopAppBar(
            title = { Text(text = titleString ?: stringResource(id = titleResId)) },
            navigationIcon = {
                if (canGoBack && onNavigateBack != null) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            },
            actions = { actions?.invoke(this) }
        )

        // Optional error or notification message bar
        val messageId = errorMessageId ?: notifyMessageId
        if (messageId != null) {
            val isError = errorMessageId != null

            Surface(
                color = if (isError) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.primaryContainer,
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
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    // Optional retry or action text if error
                    if (isError && errorAction != null && errorActionNameId != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = errorActionNameId),
                            modifier = Modifier.clickable { errorAction() },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    // Close icon to dismiss message
                    IconButton(onClick = {
                        ErrorHandler.clear()
                        NotifyHandler.clear()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }
        }
    }
}
