package com.example.matchcast.presentaion.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.matchcast.domain.model.Match

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
            match.formateDateUtc.split(" ").firstOrNull() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onBackground
            )
    ) {
        EplHeader {
            if (isSearchActive) {
                SearchButton(onClick = onSearchClick)
            } else {
                SearchInputField(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onCloseClick = onCloseSearch,
                    modifier = Modifier
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedMatches.forEach { (rawDate, matchesForDay) ->
                stickyHeader {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
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



