package com.example.matchcast.presentaion.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.matchcast.domain.model.Match
import com.example.matchcast.presentaion.screens.listmatch.states.FilterType

@Composable
fun ListMatchContent(
    listMatches: List<Match>,
    searchQuery: String,
    isSearchActive: Boolean,
    availableFilters: Map<FilterType, List<String>>,
    activeFilters: Map<FilterType, String>,
    onSearchClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
    onFilterOptionSelected: (FilterType, String) -> Unit,
    onClearFilters: () -> Unit,
    onMatchClick: (Int) -> Unit,
    onTeamClick: (String) -> Unit = {},
    onNavigateToStandings: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val groupedMatches = remember(listMatches) {
        listMatches
            .distinctBy { it.matchNumber }
            .groupBy { match -> match.formattedDateUtc.split(" ").firstOrNull() ?: "" }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        EplHeader(
            searchSlot = {
                if (isSearchActive) {
                    SearchInputField(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onCloseClick = onCloseSearch
                    )
                } else {
                    SearchButton(onClick = onSearchClick)
                }
            },
            onClickSwitchThemeButton = {},
            onClickStandingsButton = onNavigateToStandings,
            onClickFavoritesButton = onNavigateToFavorites,
            onClickAboutButton = onNavigateToAbout,
            onClickAccountButton = onNavigateToLogin
        )

        MatchFilters(
            availableFilters = availableFilters,
            activeFilters = activeFilters,
            onFilterOptionSelected = onFilterOptionSelected,
            onClearFilters = onClearFilters
        )

        if (listMatches.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Матчи не найдены",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                groupedMatches.forEach { (rawDate, matchesForDay) ->
                    item(key = "header_$rawDate") {
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            DateComp(date = rawDate, count = matchesForDay.size)
                        }
                    }
                    items(matchesForDay, key = { it.matchNumber }) { match ->
                        MatchCardItem(
                            match = match,
                            onClick = { onMatchClick(match.matchNumber) },
                        )
                    }
                }
            }
        }
    }
}
