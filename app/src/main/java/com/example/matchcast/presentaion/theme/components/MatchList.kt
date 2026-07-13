package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.matchcast.domain.model.Match
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MatchList(
    matches: List<Match>,
    onMatchClick: (Int)-> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items(matches) { match ->
            MatchCardItem(
                match = match,
                onClick = { onMatchClick(match.matchNumber) }
            )
        }
    }
}

@Preview
@Composable
fun UseMatchList() {
    MatchList(
        matches = SampleData1.sampleMatches,
        onMatchClick = {},
        modifier = Modifier.fillMaxSize()
    )
}

object SampleData1 {
    val sampleMatch = Match(
        matchNumber = 1,
        roundNumber = 1,
        formateDateUtc = "11.08.2023 19:00",
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

    val sampleMatches = listOf(
        sampleMatch,
        sampleMatch.copy(matchNumber = 2, homeTeam = "Chelsea", awayTeam = "Liverpool", location = "Stamford Bridge"),
        sampleMatch.copy(matchNumber = 3, homeTeam = "Man Utd", awayTeam = "Wolves", location = "Old Trafford"),
        sampleMatch.copy(matchNumber = 4, homeTeam = "Spurs", awayTeam = "Arsenal", location = "Tottenham Hotspur Stadium"),
        sampleMatch.copy(matchNumber = 5, homeTeam = "Man City", awayTeam = "Everton", location = "Etihad Stadium"),
        sampleMatch.copy(matchNumber = 6, homeTeam = "Newcastle", awayTeam = "Aston Villa", location = "St. James' Park", formateDateUtc = "12.08.2023 15:00", homeTeamScore = 1, awayTeamScore = 0, winner = "Newcastle"),
        sampleMatch.copy(matchNumber = 7, homeTeam = "Brentford", awayTeam = "Fulham", location = "Gtech Community Stadium", formateDateUtc = "12.08.2023 17:30", homeTeamScore = 2, awayTeamScore = 2, winner = "Draw"),
        sampleMatch.copy(matchNumber = 8, homeTeam = "Brighton", awayTeam = "West Ham", location = "Amex Stadium", formateDateUtc = "13.08.2023 14:00", homeTeamScore = 3, awayTeamScore = 1, winner = "Brighton"),
        sampleMatch.copy(matchNumber = 9, homeTeam = "Burnley", awayTeam = "Crystal Palace", location = "Turf Moor", formateDateUtc = "13.08.2023 16:30", homeTeamScore = 0, awayTeamScore = 2, winner = "Crystal Palace"),
        sampleMatch.copy(matchNumber = 10, homeTeam = "Sheffield Utd", awayTeam = "Wolves", location = "Bramall Lane", formateDateUtc = "14.08.2023 19:00", homeTeamScore = 1, awayTeamScore = 1, winner = "Draw"),
        sampleMatch.copy(matchNumber = 11, homeTeam = "Luton", awayTeam = "Bournemouth", location = "Kenilworth Road", formateDateUtc = "19.08.2023 12:30", homeTeamScore = 0, awayTeamScore = 3, winner = "Bournemouth"),
        sampleMatch.copy(matchNumber = 12, homeTeam = "Everton", awayTeam = "Nottingham Forest", location = "Goodison Park", formateDateUtc = "19.08.2023 15:00", homeTeamScore = 2, awayTeamScore = 1, winner = "Everton"),
        sampleMatch.copy(matchNumber = 13, homeTeam = "Fulham", awayTeam = "Spurs", location = "Craven Cottage", formateDateUtc = "20.08.2023 14:00", homeTeamScore = 1, awayTeamScore = 4, winner = "Spurs"),
        sampleMatch.copy(matchNumber = 14, homeTeam = "West Ham", awayTeam = "Chelsea", location = "London Stadium", formateDateUtc = "20.08.2023 16:30", homeTeamScore = 2, awayTeamScore = 2, winner = "Draw"),
        sampleMatch.copy(matchNumber = 15, homeTeam = "Aston Villa", awayTeam = "Man Utd", location = "Villa Park", formateDateUtc = "26.08.2023 17:30", homeTeamScore = 1, awayTeamScore = 3, winner = "Man Utd"),
        sampleMatch.copy(matchNumber = 16, homeTeam = "Liverpool", awayTeam = "Newcastle", location = "Anfield", formateDateUtc = "27.08.2023 15:00", homeTeamScore = 4, awayTeamScore = 1, winner = "Liverpool"),
        sampleMatch.copy(matchNumber = 17, homeTeam = "Crystal Palace", awayTeam = "Brentford", location = "Selhurst Park", formateDateUtc = "02.09.2023 15:00", homeTeamScore = 3, awayTeamScore = 2, winner = "Crystal Palace"),
        sampleMatch.copy(matchNumber = 18, homeTeam = "Arsenal", awayTeam = "Man City", location = "Emirates Stadium", formateDateUtc = "03.09.2023 16:30", homeTeamScore = 1, awayTeamScore = 2, winner = "Man City"),
        sampleMatch.copy(matchNumber = 19, homeTeam = "Leeds", awayTeam = "Brighton", location = "Elland Road", formateDateUtc = "16.09.2023 15:00", homeTeamScore = 0, awayTeamScore = 0, winner = "Draw"),
        sampleMatch.copy(matchNumber = 20, homeTeam = "Wolves", awayTeam = "Luton", location = "Molineux Stadium", formateDateUtc = "23.09.2023 12:30", homeTeamScore = 2, awayTeamScore = 0, winner = "Wolves")
    )

}
