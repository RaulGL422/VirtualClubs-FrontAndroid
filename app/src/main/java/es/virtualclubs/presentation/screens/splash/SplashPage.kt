package es.virtualclubs.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.virtualclubs.R
import es.virtualclubs.presentation.components.VCScaffold
import es.virtualclubs.presentation.theme.VCTheme
import es.virtualclubs.presentation.theme.getLargeLogo

@Composable
fun SplashPage(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Home -> onNavigateToHome()
            SplashDestination.Login -> onNavigateToLogin()
            null -> {}
        }
    }

    VCScaffold(enableTopBar = false) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(VCTheme.spacing.sectionSpacingCompact)
            ) {
                Image(
                    painter = getLargeLogo(),
                    contentDescription = stringResource(R.string.logo),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                CircularProgressIndicator(color = VCTheme.colors.primary)
            }
        }
    }
}
