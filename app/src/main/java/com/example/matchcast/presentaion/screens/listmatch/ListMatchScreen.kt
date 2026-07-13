package com.example.matchcast.presentaion.screens.listmatch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchEvent
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.ListMatchContent

@Composable
fun ListMatchScreen(
    onAction: (ListMatchAction) -> Unit,
    viewModel: ListMatchViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(ListMatchEvent.EnterScreen)
    }

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    when(val state = viewState){
        is ListMatchState.Loading -> FullScreenLoading()

        is ListMatchState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(ListMatchEvent.ReloadScreen)
            }
        )

        is ListMatchState.Display -> ListMatchContent(
            listMatches = state.listMatch,
            searchQuery = "",
            isSearchActive = false,
            onSearchClick = {
                viewModel.obtainEvent(event = ListMatchEvent.SearchQueryChanged(""))
            },
            onSearchQueryChange = {},
            onCloseSearch = {},
            onMatchClick = { matchId ->
                viewModel.obtainEvent(event = ListMatchEvent.OnMatchClick(matchId))
            },
            modifier = Modifier
        )

        is ListMatchState.Search -> ListMatchContent(
            listMatches = state.results,
            searchQuery = state.query,
            isSearchActive = true,
            onSearchClick = {},
            onSearchQueryChange = { newQuery ->
                viewModel.obtainEvent(ListMatchEvent.SearchQueryChanged(newQuery))
            },
            onCloseSearch = {
                viewModel.obtainEvent(ListMatchEvent.SearchClear)
            },
            onMatchClick = { matchId ->
                viewModel.obtainEvent(event = ListMatchEvent.OnMatchClick(matchId))
            },
            modifier = Modifier
        )
    }
}