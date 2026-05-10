package es.virtualclubs.presentation.screens.settings

import android.app.LocaleManager
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.virtualclubs.BuildConfig
import es.virtualclubs.R
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.components.VCScaffold
import es.virtualclubs.presentation.managers.LocalGlobalUIManager
import es.virtualclubs.presentation.theme.VCTheme
import es.virtualclubs.presentation.theme.getAppVersion
import kotlin.system.exitProcess

@Composable
fun SettingsPage(
    screenType: ScreenType,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val globalUIManager = LocalGlobalUIManager.current

    LaunchedEffect(Unit) {
        viewModel.restartSignal.collect {
            val intent = context.packageManager
                .getLaunchIntentForPackage(context.packageName) ?: return@collect
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            exitProcess(0)
        }
    }

    VCScaffold(
        titleTopBar = R.string.settings_page,
        canGoBack = true,
        onNavigateBack = onBack
    ) { paddingValues ->
        val spacing = VCTheme.spacing
        val columnWidthFraction = when (screenType) {
            ScreenType.Small  -> 0.85f
            ScreenType.Medium -> 0.45f
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(columnWidthFraction)
                    .padding(vertical = spacing.screenVertical),
                verticalArrangement = Arrangement.spacedBy(spacing.md)
            ) {
                if (uiState.isLoggedIn) {
                    AccountCard(email = uiState.email ?: "")
                    Spacer(Modifier.height(spacing.md))
                }

                SectionHeader(stringResource(R.string.settings_device))

                SettingRow(
                    title    = stringResource(R.string.settings_language),
                    subtitle = stringResource(R.string.settings_language_subtitle),
                    icon     = Icons.Filled.Language,
                    onClick  = {
                        globalUIManager.showDialog(
                            LanguagePickerDialog(
                                currentLanguage    = uiState.appLanguage,
                                onLanguageSelected = { tag ->
                                    viewModel.setAppLanguage(tag)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        context.getSystemService(LocaleManager::class.java)
                                            .applicationLocales =
                                            if (tag.isEmpty()) LocaleList.getEmptyLocaleList()
                                            else LocaleList.forLanguageTags(tag)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.settings_language_restart_required),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        )
                    }
                )

                SettingToggleRow(
                    title    = stringResource(R.string.settings_notifications),
                    subtitle = stringResource(R.string.settings_notifications_subtitle),
                    icon     = Icons.Filled.Notifications,
                    checked  = uiState.notificationsEnabled,
                    onToggle = viewModel::setNotificationsEnabled
                )

                Spacer(Modifier.height(spacing.sm))
                HorizontalDivider(color = VCTheme.colors.outlineVariant)
                Spacer(Modifier.height(spacing.sm))

                SectionHeader(stringResource(R.string.settings_appearance))

                AppearanceSection(
                    uiState         = uiState,
                    onThemeChange   = viewModel::setTheme,
                    onContrastChange = viewModel::setContrast,
                    onFontSizeChange = viewModel::setFontSize
                )

                Spacer(Modifier.height(spacing.sm))
                HorizontalDivider(color = VCTheme.colors.outlineVariant)
                Spacer(Modifier.height(spacing.sm))

                SectionHeader(stringResource(R.string.settings_about))

                SettingRow(
                    title    = stringResource(R.string.settings_privacy_policy),
                    subtitle = stringResource(R.string.settings_privacy_policy_subtitle),
                    icon     = Icons.Filled.Shield,
                    onClick  = null
                )

                SettingRow(
                    title    = stringResource(R.string.settings_terms),
                    subtitle = stringResource(R.string.settings_terms_subtitle),
                    icon     = Icons.Filled.Article,
                    onClick  = null
                )

                if (BuildConfig.DEBUG) {
                    Spacer(Modifier.height(spacing.sm))
                    HorizontalDivider(color = VCTheme.colors.outlineVariant)
                    Spacer(Modifier.height(spacing.sm))
                    DebugServerSection(
                        currentUrl = uiState.debugServerUrl,
                        onSave     = viewModel::saveDebugServerUrl
                    )
                }

                Spacer(Modifier.height(spacing.md))
                Text(
                    text      = getAppVersion(context),
                    style     = VCTheme.typography.bodySmall,
                    color     = VCTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(spacing.xl))
            }
        }
    }
}

@Composable
private fun AccountCard(email: String) {
    val spacing = VCTheme.spacing

    Surface(
        shape    = VCTheme.shapes.medium,
        color    = VCTheme.colors.surfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier              = Modifier.padding(spacing.cardPadding),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.inlineSpacingMd)
        ) {
            Icon(
                imageVector    = Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier       = Modifier.size(40.dp),
                tint           = VCTheme.colors.primary
            )
            Column {
                Text(
                    text  = stringResource(R.string.settings_account),
                    style = VCTheme.typography.labelSmall,
                    color = VCTheme.colors.onSurfaceVariant
                )
                Text(
                    text  = email,
                    style = VCTheme.typography.bodyMedium,
                    color = VCTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text       = text,
        style      = VCTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color      = VCTheme.colors.onSurface
    )
}
