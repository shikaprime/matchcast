package com.example.matchcast.presentaion.theme.components

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import coil3.compose.AsyncImage
import com.example.matchcast.data.repository.teamLogoDrawableMap
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.presentaion.theme.EplNeonGreen
import com.example.matchcast.presentaion.theme.EplPurple
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.example.matchcast.presentaion.theme.TextMuted
import com.example.matchcast.presentaion.theme.utils.advancedShadow
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDate
import com.microsoft.fluent.mobile.icons.R


@Composable
fun MatchCardItem(
    match: Match,
    onClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) //8
            .advancedShadow(Color.Black, alpha = 0.15f, 8.dp, 4.dp, 4.dp)
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.surface
                ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dateParts = match.formateDateUtc.split(" ")
                val formattedDate = formatSimpleDate(dateParts.first())
                val time = dateParts.lastOrNull() ?: ""
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(60.dp)
                            .background(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.onBackground.copy(0.9f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = EplNeonGreen
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "Матч №${match.matchNumber}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
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
                    AsyncImage(
                        model = teamLogoDrawableMap[match.homeTeam],
                        contentDescription = "${match.homeTeam} logo",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = match.homeTeam,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                    )
                }
                Text(
                    text = "${match.homeTeamScore} : ${match.awayTeamScore}",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EplPurple
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = teamLogoDrawableMap[match.awayTeam],
                        contentDescription = "${match.awayTeam} logo",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = match.awayTeam,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(Modifier, thickness = 1.dp, color = Color(0xFFF0ECF1))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_fluent_location_24_regular),
                        contentDescription = "location",
                        modifier = Modifier.size(12.dp),
                    )
                    Text(
                        text = match.location,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                FormGuide(
                    form = match.homeTeamForm,
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
        }
    }
}


fun formatSimpleDate(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        parsedDate.format(DateTimeFormatter.ofPattern("dd MMM", Locale("ru")))
    } catch (e: Exception) {
        dateString
    }
}



// ИСПРАВЛЕН ПРЕВЬЮЕР
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
}

val homeForm = ArrayDeque(listOf(
    MatchOutcome.WIN,
    MatchOutcome.DRAW,
    MatchOutcome.LOSE,
    MatchOutcome.WIN,
    MatchOutcome.LOSE
))

val awayForm = ArrayDeque(listOf(
    MatchOutcome.LOSE,
    MatchOutcome.WIN,
    MatchOutcome.DRAW,
    MatchOutcome.LOSE,
    MatchOutcome.WIN
))
