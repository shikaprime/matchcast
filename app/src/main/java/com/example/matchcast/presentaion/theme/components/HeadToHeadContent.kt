package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.data.repository.teamLogoDrawableMap
import com.example.matchcast.domain.model.HeadToHead
import com.example.matchcast.domain.model.Match
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun HeadToHeadContent(
    headToHead: HeadToHead,
    onBackClick: () -> Unit,
    onMatchClick: (Int) -> Unit,
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
                text = "Личные встречи",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
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
                    HeadToHeadSummaryCard(headToHead = headToHead)
                }
                item {
                    Text(
                        text = "История встреч (${headToHead.totalMatches})".uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (headToHead.matches.isEmpty()) {
                    item {
                        Text(
                            text = "Эти команды ещё не встречались",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                items(headToHead.matches, key = { it.matchNumber }) { match ->
                    MatchCardItem(
                        match = match,
                        onClick = { onMatchClick(match.matchNumber) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeadToHeadSummaryCard(headToHead: HeadToHead, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HeadToHeadTeamColumn(teamName = headToHead.teamA, modifier = Modifier.weight(1f))
            Text(
                text = "VS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HeadToHeadTeamColumn(teamName = headToHead.teamB, modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HeadToHeadStat(value = headToHead.teamAWins, label = "Победы", modifier = Modifier.weight(1f))
            HeadToHeadStat(value = headToHead.draws, label = "Ничьи", modifier = Modifier.weight(1f))
            HeadToHeadStat(value = headToHead.teamBWins, label = "Победы", modifier = Modifier.weight(1f))
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Голы: ${headToHead.teamAGoals}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Голы: ${headToHead.teamBGoals}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun HeadToHeadTeamColumn(teamName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        teamLogoDrawableMap[teamName]?.let { drawableId ->
            Image(
                painter = painterResource(drawableId),
                contentDescription = "$teamName logo",
                modifier = Modifier.size(48.dp)
            )
        }
        Text(
            text = teamName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HeadToHeadStat(value: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UseHeadToHeadContent() {
    MatchCastTheme {
        HeadToHeadContent(
            headToHead = HeadToHead(
                teamA = "Arsenal",
                teamB = "Chelsea",
                teamAWins = 5,
                teamBWins = 3,
                draws = 2,
                teamAGoals = 14,
                teamBGoals = 9,
                matches = listOf(
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
