package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.data.repository.teamLogoDrawableMap
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.presentaion.screens.team.states.TeamState
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun TeamContent(
    state: TeamState.Display,
    onBackClick: () -> Unit,
    onMatchClick: (Int) -> Unit,
    onTeamClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BackButton(onClick = onBackClick)
            Text(
                text = "Команда",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            teamLogoDrawableMap[state.teamName]?.let { drawableId ->
                Image(
                    painter = painterResource(drawableId),
                    contentDescription = "${state.teamName} logo",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = state.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    VenueCard(geo = state.stadium)
                }
                item {
                    TeamFormCard(form = state.form)
                }
                item {
                    Text(
                        text = "Последние результаты".uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(state.recentMatches, key = { it.matchNumber }) { match ->
                    MatchCardItem(
                        match = match,
                        onClick = { onMatchClick(match.matchNumber) },
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamFormCard(form: ArrayDeque<MatchOutcome>?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Форма (последние 5 матчей)".uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.primary
        )
        FormGuide(form = form, modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun UseTeamContent() {
    MatchCastTheme {
        TeamContent(
            state = TeamState.Display(
                teamName = "Arsenal",
                fullName = "Arsenal FC",
                stadium = "Emirates Stadium",
                form = ArrayDeque(
                    listOf(MatchOutcome.WIN, MatchOutcome.WIN, MatchOutcome.DRAW, MatchOutcome.LOSE, MatchOutcome.WIN)
                ),
                recentMatches = listOf(
                    Match(
                        matchNumber = 1,
                        roundNumber = 1,
                        formattedDateUtc = "11.08.2023 19:00",
                        location = "Emirates Stadium",
                        homeTeam = "Arsenal",
                        awayTeam = "Chelsea",
                        group = null,
                        homeTeamScore = 3,
                        awayTeamScore = 1,
                        winner = "Arsenal",
                        homeTeamForm = null,
                        awayTeamForm = null
                    )
                )
            ),
            onBackClick = {},
            onMatchClick = {}
        )
    }
}
