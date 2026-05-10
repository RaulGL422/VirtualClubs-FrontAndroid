package es.virtualclubs.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.virtualclubs.R
import es.virtualclubs.domain.model.Club
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle
import es.virtualclubs.presentation.components.VCScaffold
import androidx.compose.material3.Surface

@Composable
fun HomePage(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(uiState = uiState)
}

@Composable
private fun HomeContent(uiState: HomeUiState) {
    VCScaffold(
        titleTopBar = R.string.home_page
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                HomeHeader(email = uiState.userEmail)
            }

            item {
                Text(
                    text = stringResource(R.string.home_clubs_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (uiState.clubs.isEmpty()) {
                item {
                    HomeEmptyClubs()
                }
            } else {
                items(uiState.clubs, key = { it.id }) { club ->
                    ClubCard(club = club)
                }
            }

            item {
                HomeActions()
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun HomeHeader(email: String?) {
    val greeting = if (!email.isNullOrBlank()) {
        stringResource(R.string.home_greeting, email)
    } else {
        stringResource(R.string.home_greeting_anonymous)
    }

    Text(
        text = greeting,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun HomeEmptyClubs() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.home_no_clubs_title),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.home_no_clubs_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ClubCard(club: Club) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = club.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = club.sport,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.home_members, club.memberCount),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HomeActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VCButton(
            content = VCButtonContent.Text(R.string.home_create_club),
            style = VCButtonStyle.Primary,
            modifier = Modifier.weight(1f),
            onClick = { /* TODO: navegar a crear club */ }
        )
        VCButton(
            content = VCButtonContent.Text(R.string.home_join_club),
            style = VCButtonStyle.Outline,
            modifier = Modifier.weight(1f),
            onClick = { /* TODO: navegar a unirse a club */ }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentEmptyPreview() {
    Surface {
        HomeContent(
            uiState = HomeUiState(userEmail = "usuario@ejemplo.com", clubs = emptyList())
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentWithClubsPreview() {
    Surface {
        HomeContent(
            uiState = HomeUiState(
                userEmail = "usuario@ejemplo.com",
                clubs = listOf(
                    Club(id = "1", name = "FC Barcelona Fan Club", sport = "Fútbol", memberCount = 42),
                    Club(id = "2", name = "Lakers Zone", sport = "Baloncesto", memberCount = 17),
                    Club(id = "3", name = "Padel Warriors", sport = "Pádel", memberCount = 8)
                )
            )
        )
    }
}
