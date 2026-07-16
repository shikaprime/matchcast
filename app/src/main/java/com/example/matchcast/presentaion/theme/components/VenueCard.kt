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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.presentaion.theme.MatchCastTheme
import com.example.matchcast.presentaion.theme.utils.advancedShadow
import com.microsoft.fluent.mobile.icons.R

@Composable
fun VenueCard(
    geo: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .advancedShadow(Color.Black, alpha = 0.15f, cornersRadius = 8.dp, offsetX = 0.dp, offsetY = 0.dp,shadowBlurRadius = 12.dp)

        .background(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_fluent_location_24_regular),
                contentDescription = "geolocation",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            horizontalAlignment = AbsoluteAlignment.Left,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Место проведения".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = geo,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UseVenueCard(){
    MatchCastTheme {
        VenueCard("Turf Moor")
    }
}