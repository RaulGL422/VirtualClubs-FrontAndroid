package es.virtualclubs.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.virtualclubs.R
import es.virtualclubs.presentation.theme.VCTheme

private data class PasswordRequirement(
    @param:StringRes val labelRes: Int,
    val check: (String) -> Boolean
)

private val passwordRequirements = listOf(
    PasswordRequirement(R.string.password_req_min_length) { it.length >= 6 },
    PasswordRequirement(R.string.password_req_letter) { it.any(Char::isLetter) },
    PasswordRequirement(R.string.password_req_number) { it.any(Char::isDigit) },
)

fun meetsAllPasswordRequirements(password: String): Boolean =
    passwordRequirements.all { it.check(password) }

@Composable
fun PasswordRequirements(
    password: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(VCTheme.spacing.itemSpacingCompact)
    ) {
        passwordRequirements.forEach { req ->
            val met = password.isNotEmpty() && req.check(password)
            val color = if (met) VCTheme.colors.primary else VCTheme.colors.onSurfaceVariant
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(VCTheme.spacing.inlineSpacingSm)
            ) {
                Icon(
                    imageVector = if (met) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(VCTheme.sizes.iconXs)
                )
                Text(
                    text = stringResource(req.labelRes),
                    style = VCTheme.typography.labelSmall,
                    color = color
                )
            }
        }
    }
}
