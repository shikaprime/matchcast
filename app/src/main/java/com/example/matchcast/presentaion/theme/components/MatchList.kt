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

    val sampleMatches = listOf(
        sampleMatch,
        sampleMatch.copy(matchNumber = 2, homeTeam = "Chelsea", awayTeam = "Liverpool", location = "Stamford Bridge"),
        sampleMatch.copy(matchNumber = 3, homeTeam = "Man Utd", awayTeam = "Wolves", location = "Old Trafford"),
        sampleMatch.copy(matchNumber = 4, homeTeam = "Spurs", awayTeam = "Arsenal", location = "Tottenham Hotspur Stadium"),
        sampleMatch.copy(matchNumber = 5, homeTeam = "Man City", awayTeam = "Everton", location = "Etihad Stadium"),
        sampleMatch.copy(matchNumber = 6, homeTeam = "Newcastle", awayTeam = "Aston Villa", location = "St. James' Park", formattedDateUtc = "12.08.2023 15:00", homeTeamScore = 1, awayTeamScore = 0, winner = "Newcastle"),
        sampleMatch.copy(matchNumber = 7, homeTeam = "Brentford", awayTeam = "Fulham", location = "Gtech Community Stadium", formattedDateUtc = "12.08.2023 17:30", homeTeamScore = 2, awayTeamScore = 2, winner = "Draw"),
        sampleMatch.copy(matchNumber = 8, homeTeam = "Brighton", awayTeam = "West Ham", location = "Amex Stadium", formattedDateUtc = "13.08.2023 14:00", homeTeamScore = 3, awayTeamScore = 1, winner = "Brighton"),
        sampleMatch.copy(matchNumber = 9, homeTeam = "Burnley", awayTeam = "Crystal Palace", location = "Turf Moor", formattedDateUtc = "13.08.2023 16:30", homeTeamScore = 0, awayTeamScore = 2, winner = "Crystal Palace"),
        sampleMatch.copy(matchNumber = 10, homeTeam = "Sheffield Utd", awayTeam = "Wolves", location = "Bramall Lane", formattedDateUtc = "14.08.2023 19:00", homeTeamScore = 1, awayTeamScore = 1, winner = "Draw"),
        sampleMatch.copy(matchNumber = 11, homeTeam = "Luton", awayTeam = "Bournemouth", location = "Kenilworth Road", formattedDateUtc = "19.08.2023 12:30", homeTeamScore = 0, awayTeamScore = 3, winner = "Bournemouth"),
        sampleMatch.copy(matchNumber = 12, homeTeam = "Everton", awayTeam = "Nottingham Forest", location = "Goodison Park", formattedDateUtc = "19.08.2023 15:00", homeTeamScore = 2, awayTeamScore = 1, winner = "Everton"),
        sampleMatch.copy(matchNumber = 13, homeTeam = "Fulham", awayTeam = "Spurs", location = "Craven Cottage", formattedDateUtc = "20.08.2023 14:00", homeTeamScore = 1, awayTeamScore = 4, winner = "Spurs"),
        sampleMatch.copy(matchNumber = 14, homeTeam = "West Ham", awayTeam = "Chelsea", location = "London Stadium", formattedDateUtc = "20.08.2023 16:30", homeTeamScore = 2, awayTeamScore = 2, winner = "Draw"),
        sampleMatch.copy(matchNumber = 15, homeTeam = "Aston Villa", awayTeam = "Man Utd", location = "Villa Park", formattedDateUtc = "26.08.2023 17:30", homeTeamScore = 1, awayTeamScore = 3, winner = "Man Utd"),
        sampleMatch.copy(matchNumber = 16, homeTeam = "Liverpool", awayTeam = "Newcastle", location = "Anfield", formattedDateUtc = "27.08.2023 15:00", homeTeamScore = 4, awayTeamScore = 1, winner = "Liverpool"),
        sampleMatch.copy(matchNumber = 17, homeTeam = "Crystal Palace", awayTeam = "Brentford", location = "Selhurst Park", formattedDateUtc = "02.09.2023 15:00", homeTeamScore = 3, awayTeamScore = 2, winner = "Crystal Palace"),
        sampleMatch.copy(matchNumber = 18, homeTeam = "Arsenal", awayTeam = "Man City", location = "Emirates Stadium", formattedDateUtc = "03.09.2023 16:30", homeTeamScore = 1, awayTeamScore = 2, winner = "Man City"),
        sampleMatch.copy(matchNumber = 19, homeTeam = "Leeds", awayTeam = "Brighton", location = "Elland Road", formattedDateUtc = "16.09.2023 15:00", homeTeamScore = 0, awayTeamScore = 0, winner = "Draw"),
        sampleMatch.copy(matchNumber = 20, homeTeam = "Wolves", awayTeam = "Luton", location = "Molineux Stadium", formattedDateUtc = "23.09.2023 12:30", homeTeamScore = 2, awayTeamScore = 0, winner = "Wolves")
    )

}
