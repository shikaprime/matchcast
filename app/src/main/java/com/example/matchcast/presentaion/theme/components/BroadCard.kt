package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.example.matchcast.presentaion.theme.TextPrimary
import com.example.matchcast.presentaion.theme.utils.advancedShadow
import com.microsoft.fluent.mobile.icons.R


@Composable
fun BroadCard(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .advancedShadow(
                color = Color.Black,
                alpha = 0.15f,
                cornersRadius = 16.dp,
                offsetX = 0.dp,
                offsetY = 0.dp,
                shadowBlurRadius = 12.dp
            )
            .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_fluent_tv_24_regular),
                contentDescription = "Трансляция",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Трансляция".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChannelChip(text = "DAZN")
                ChannelChip(text = "Sky Sports")
                ChannelChip(text = "Peacock")
                ChannelChip(text = "Paramount+")
            }
        }
    }
}

@Composable
private fun ChannelChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(6.dp),
                // Fixed dark scrim (not colorScheme.onBackground) so it stays dark in
                // both themes - it's paired with the fixed neon-green secondary text
                // below, which needs a dark backdrop regardless of overall theme.
                color = TextPrimary.copy(alpha = 0.9f)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
@Preview
@Composable
fun UseBroadCard(){
    MatchCastTheme {
        BroadCard()
    }
}