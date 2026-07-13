package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.microsoft.fluent.mobile.icons.R

@Composable
fun MatchHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    match: Match,
) {
    val dateParts = match.formateDateUtc.split(" ")
    val formattedDate = formSimpleDate(dateParts.first())
    val time = dateParts.lastOrNull() ?: ""
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 20.dp),

    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BackButton(onClick = onBackClick)
            Text(
                text = "Премьер Лига",
                color = MaterialTheme.colorScheme.surface,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp

            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50.dp)
                )
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formattedDate.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_fluent_clock_24_regular),
                        contentDescription = "",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = time,
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
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
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = match.homeTeam,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.surface,
                )
            }
            Text(
                text = "${match.homeTeamScore} : ${match.awayTeamScore}",
                style = MaterialTheme.typography.labelMedium,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.surface
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = teamLogoDrawableMap[match.awayTeam],
                    contentDescription = "${match.awayTeam} logo",
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = match.awayTeam,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UseMatchHeaderPreview() {
    MatchCastTheme {
        MatchHeader(
            match = Match(
                formateDateUtc = "11.08.2023 19:00",
                homeTeam = "Arsenal",
                awayTeam = "Man City",
                homeTeamScore = 3,
                awayTeamScore = 1,
                matchNumber = 1,
                roundNumber = 38,
                location = "Emirates Stadium",
                group = null,
                winner = "Arsenal",
                homeTeamForm = null,
                awayTeamForm = null
            ),
            onBackClick = {}
        )
    }
}