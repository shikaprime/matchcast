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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matchcast.R
import com.example.matchcast.presentaion.theme.MatchCastTheme

@Composable
fun EplHeader(
    modifier: Modifier = Modifier,
    searchSlot: @Composable () -> Unit,
    onClickSwitchThemeButton: () -> Unit,
    onClickStandingsButton: () -> Unit = {},
    onClickFavoritesButton: () -> Unit = {},
    onClickAboutButton: () -> Unit = {},
    onClickAccountButton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.premier_league),
                contentDescription = "Премьер лига",
                modifier = Modifier.size(150.dp)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StandingsButton(onClick = onClickStandingsButton)
                FavoritesButton(onClick = onClickFavoritesButton)
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountButton(onClick = onClickAccountButton)
                AboutButton(onClick = onClickAboutButton)
                SwitchThemeButton(onClick = onClickSwitchThemeButton)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        searchSlot()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun UseEplHeader() {
    MatchCastTheme {
        EplHeader(searchSlot = { SearchButton(onClick = {}) },
            onClickSwitchThemeButton = {},
            onClickStandingsButton = {})
    }
}

@Composable
fun StandingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Leaderboard,
            contentDescription = "Таблица",
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun FavoritesButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Избранное",
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun AccountButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Аккаунт",
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun AboutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "О приложении",
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun SwitchThemeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.08f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.LightMode,
            contentDescription = "Switch Theme",
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UseSwitchThemeButton(){
    MatchCastTheme {
        SwitchThemeButton() { }
    }
}
