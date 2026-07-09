package com.example.matchcast.presentaion.screens.listmatch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchEvent
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.MatchList
import com.example.matchcast.presentaion.theme.components.MatchSearchbar

@Composable
fun ListMatchScreen(
    onAction: (ListMatchAction) -> Unit,
    viewModel: ListMatchViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) {
        viewModel.viewAction.collect {
            action -> onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(ListMatchEvent.EnterScreen)
    }

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewState is ListMatchState.Display || viewState is ListMatchState.Search){
                val currentQuery = (viewState as? ListMatchState.Search)?.query ?: ""

                MatchSearchbar(
                    query = currentQuery,
                    onQueryChanged = {
                        text -> viewModel.obtainEvent(ListMatchEvent.SearchQueryChanged(text))
                    },
                    onClearPressed = {
                        viewModel.obtainEvent(ListMatchEvent.SearchClear)
                    }
                )
            }
            when(val state = viewState){
                is ListMatchState.Loading -> {
                    FullScreenLoading()
                }

                is ListMatchState.Display -> {
                    MatchList(
                        matches = state.listMatch,
                        onMatchClick = {
                            matchId -> viewModel.obtainEvent(event = ListMatchEvent.OnMatchClick(matchId))
                        }
                    )
                }

                is ListMatchState.Error -> {
                    FullScreenError(
                        iconRes = state.icon,
                        message = state.description,
                        onRetry = { viewModel.obtainEvent(ListMatchEvent.ReloadScreen) }
                    )
                }

                is ListMatchState.Search -> {
                    if(state.isLoading){
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }else{
                        MatchList(
                          matches = state.results,
                            onMatchClick = { matchId -> viewModel.obtainEvent(ListMatchEvent.OnMatchClick(matchId))}
                        )
                    }
                }
            }
        }
    }
}