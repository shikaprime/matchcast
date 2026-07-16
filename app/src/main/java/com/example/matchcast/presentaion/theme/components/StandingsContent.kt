package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.data.repository.teamLogoDrawableMap
import com.example.matchcast.domain.model.Standing
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun StandingsContent(
    standings: List<Standing>,
    onBackClick: () -> Unit,
    onTeamClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BackButton(onClick = onBackClick)
            Text(
                text = "Таблица",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        StandingsHeaderRow()
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))

        LazyColumn {
            items(standings, key = { it.teamName }) { standing ->
                StandingsRow(
                    standing = standing,
                    onClick = { onTeamClick(standing.teamName) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }
        }
    }
}

@Composable
private fun StandingsHeaderRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#",
            modifier = Modifier.width(28.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Команда",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StandingsStatHeaderCell("И")
        StandingsStatHeaderCell("В")
        StandingsStatHeaderCell("Н")
        StandingsStatHeaderCell("П")
        StandingsStatHeaderCell("О")
    }
}

@Composable
private fun StandingsStatHeaderCell(label: String) {
    Text(
        text = label,
        modifier = Modifier.width(28.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StandingsRow(
    standing: Standing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = standing.position.toString(),
            modifier = Modifier.width(28.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            teamLogoDrawableMap[standing.teamName]?.let { drawableId ->
                Image(
                    painter = painterResource(drawableId),
                    contentDescription = "${standing.teamName} logo",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = standing.teamName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        StandingsStatCell(standing.played)
        StandingsStatCell(standing.wins)
        StandingsStatCell(standing.draws)
        StandingsStatCell(standing.losses)
        Text(
            text = standing.points.toString(),
            modifier = Modifier.width(28.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun StandingsStatCell(value: Int) {
    Text(
        text = value.toString(),
        modifier = Modifier.width(28.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
fun UseStandingsContent() {
    MatchCastTheme {
        StandingsContent(
            standings = listOf(
                Standing(1, "Arsenal", 10, 8, 1, 1, 24, 8, 25),
                Standing(2, "Man City", 10, 7, 2, 1, 22, 10, 23),
                Standing(3, "Liverpool", 10, 6, 3, 1, 20, 11, 21)
            ),
            onBackClick = {},
            onTeamClick = {}
        )
    }
}
