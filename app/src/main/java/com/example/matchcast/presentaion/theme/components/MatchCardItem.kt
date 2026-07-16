package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.data.repository.teamLogoDrawableMap
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.example.matchcast.presentaion.theme.utils.advancedShadow

@Composable
fun MatchCardItem(
    match: Match,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .advancedShadow(Color.Black, alpha = 0.15f, 8.dp, 4.dp, 4.dp)
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val time = match.formattedDateUtc.split(" ").lastOrNull()?.take(5) ?: ""
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(60.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    teamLogoDrawableMap[match.homeTeam]?.let { drawableId ->
                        Image(
                            painter = painterResource(drawableId),
                            contentDescription = "${match.homeTeam} logo",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                    Text(
                        text = match.homeTeam,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Text(
                    text = "${match.homeTeamScore} : ${match.awayTeamScore}",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    teamLogoDrawableMap[match.awayTeam]?.let { drawableId ->
                        Image(
                            painter = painterResource(drawableId),
                            contentDescription = "${match.awayTeam} logo",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                    Text(
                        text = match.awayTeam,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UseMatchCardItem() {
    MatchCastTheme {
        MatchCardItem(match = SampleData.sampleMatch)
    }
}

object SampleData {
    val sampleMatch = Match(
        matchNumber = 1,
        roundNumber = 1,
        formattedDateUtc = "11.08.2023 19:00",
        location = "Turf Moor",
        homeTeam = "Arsenal",
        awayTeam = "Nottingham Forest",
        homeTeamScore = 0,
        awayTeamScore = 3,
        winner = "Man City",
        group = null,
        homeTeamForm = homeForm,
        awayTeamForm = awayForm
    )
}

val homeForm = ArrayDeque(
    listOf(
        MatchOutcome.WIN,
        MatchOutcome.DRAW,
        MatchOutcome.LOSE,
        MatchOutcome.WIN,
        MatchOutcome.LOSE
    )
)

val awayForm = ArrayDeque(
    listOf(
        MatchOutcome.LOSE,
        MatchOutcome.WIN,
        MatchOutcome.DRAW,
        MatchOutcome.LOSE,
        MatchOutcome.WIN
    )
)
