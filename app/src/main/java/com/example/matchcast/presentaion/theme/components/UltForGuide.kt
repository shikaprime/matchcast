package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.domain.model.MatchOutcome
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.example.matchcast.presentaion.theme.utils.advancedShadow

@Composable
fun UltFormGuide(
    modifier: Modifier = Modifier,
    homeTeam: String,
    awayTeam: String,
    homeTeamForm: ArrayDeque<MatchOutcome>?,
    awayTeamForm: ArrayDeque<MatchOutcome>?
){
    Column (
        modifier = modifier
            .fillMaxWidth()
            .advancedShadow(Color.Black, alpha = 0.15f, cornersRadius = 8.dp, offsetX = 0.dp, offsetY = 0.dp,shadowBlurRadius = 12.dp)
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Предыдущие матчи".uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = homeTeam,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSecondary
            )
            FormGuide(
                modifier = modifier,
                form = homeTeamForm
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = awayTeam,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSecondary
            )
            FormGuide(
                modifier = Modifier,
                form = awayTeamForm
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UseUltForGuide(){
    MatchCastTheme {
        val homeForm = ArrayDeque(
            listOf(
                MatchOutcome.WIN,
                MatchOutcome.WIN,
                MatchOutcome.DRAW,
                MatchOutcome.LOSE,
                MatchOutcome.WIN
            )
        )

        val awayForm = ArrayDeque(
            listOf(
                MatchOutcome.DRAW,
                MatchOutcome.WIN,
                MatchOutcome.WIN,
                MatchOutcome.WIN,
                MatchOutcome.LOSE
            )
        )

        UltFormGuide(
            homeTeam = "Arsenal",
            awayTeam = "Man City",
            homeTeamForm = homeForm,
            awayTeamForm = awayForm
        )
    }
}