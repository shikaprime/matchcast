package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchcast.R
import com.example.matchcast.domain.model.Match
import com.example.matchcast.presentaion.theme.BackgroundLight

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListMatchContent(
    listMatches: List<Match>,
    searchQuery: String,
    isSearchActive: Boolean,
    onSearchClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
    onMatchClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedMatches = remember(listMatches) {
        listMatches.groupBy { match ->
            match.formattedDateUtc.split(" ").firstOrNull() ?: ""
        }
    }

    val maxLogoHeight = 180.dp
    val minLogoHeight = 0.dp
    val maxLogoHeightPx = with(LocalDensity.current) { maxLogoHeight.toPx() }
    val minLogoHeightPx = with(LocalDensity.current) { minLogoHeight.toPx() }

    var logoHeightPx by remember { mutableFloatStateOf(maxLogoHeightPx) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < 0) {
                    val previousHeight = logoHeightPx
                    logoHeightPx = (logoHeightPx + delta).coerceIn(minLogoHeightPx, maxLogoHeightPx)
                    return Offset(0f, logoHeightPx - previousHeight)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                if (delta > 0) {
                    val previousHeight = logoHeightPx
                    logoHeightPx = (logoHeightPx + delta).coerceIn(minLogoHeightPx, maxLogoHeightPx)
                    return Offset(0f, logoHeightPx - previousHeight)
                }
                return Offset.Zero
            }
        }
    }

    val logoHeightDp = with(LocalDensity.current) { logoHeightPx.toDp() }
    val logoAlpha = logoHeightPx / maxLogoHeightPx

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackgroundLight)
            .statusBarsPadding()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .height(logoHeightDp)
                .graphicsLayer { alpha = logoAlpha }
                .clipToBounds(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.premier_league),
                    contentDescription = "Премьер лига",
                    modifier = Modifier.size(130.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Результаты матчей",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (isSearchActive) {
                SearchInputField(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onCloseClick = onCloseSearch,
                    modifier = Modifier
                )
            } else {
                SearchButton(onClick = onSearchClick)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedMatches.forEach { (rawDate, matchesForDay) ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        DateComp(
                            date = rawDate,
                            count = matchesForDay.size
                        )
                    }
                }

                items(
                    items = matchesForDay,
                    key = { it.matchNumber }
                ) { match ->
                    MatchCardItem(
                        match = match,
                        onClick = { onMatchClick(match.matchNumber) }
                    )
                }
            }
        }
    }
}
