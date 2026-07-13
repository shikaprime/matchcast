package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun DetailMatchContent(
    match: Match,
    onBackCLick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ){
        MatchHeader(
            match = match,
            onBackClick = onBackCLick
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    VenueCard(
                        geo = match.location
                    )
                }
                item {
                    BroadCard()
                }
                item {
                    UltFormGuide(
                        modifier = Modifier,
                        homeTeam = match.homeTeam,
                        awayTeam = match.awayTeam,
                        homeTeamForm = match.homeTeamForm,
                        awayTeamForm = match.awayTeamForm
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMatchContentPreview() {
    MatchCastTheme {
        val sampleHomeForm = ArrayDeque(
            listOf(
                MatchOutcome.WIN,
                MatchOutcome.WIN,
                MatchOutcome.DRAW,
                MatchOutcome.LOSE,
                MatchOutcome.WIN
            )
        )

        val sampleAwayForm = ArrayDeque(
            listOf(
                MatchOutcome.DRAW,
                MatchOutcome.WIN,
                MatchOutcome.WIN,
                MatchOutcome.WIN,
                MatchOutcome.LOSE
            )
        )

        val previewMatch = Match(
            matchNumber = 380,
            roundNumber = 38,
            formateDateUtc = "12.07.2026 16:30",
            location = "Emirates Stadium",
            homeTeam = "Arsenal",
            awayTeam = "Man City",
            group = null,
            homeTeamScore = 3,
            awayTeamScore = 1,
            winner = "Arsenal",
            homeTeamForm = sampleHomeForm,
            awayTeamForm = sampleAwayForm
        )

        DetailMatchContent(
            match = previewMatch,
            onBackCLick = {}
        )
    }
}