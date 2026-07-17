package com.example.matchcast.presentaion.screens.standings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.standings.states.StandingsAction
import com.example.matchcast.presentaion.screens.standings.states.StandingsEvent
import com.example.matchcast.presentaion.screens.standings.states.StandingsState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.StandingsContent

@Composable
fun StandingsScreen(
    onAction: (StandingsAction) -> Unit,
    viewModel: StandingsViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(StandingsEvent.EnterScreen)
    }

    when (val state = viewState) {
        is StandingsState.Loading -> FullScreenLoading()
        is StandingsState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(StandingsEvent.ReloadScreen)
            }
        )
        is StandingsState.Display -> StandingsContent(
            standings = state.standings,
            favoriteTeamNames = state.favoriteTeamNames,
            onBackClick = {
                viewModel.obtainEvent(StandingsEvent.OnBackClick)
            },
            onTeamClick = { teamName ->
                viewModel.obtainEvent(StandingsEvent.OnTeamClick(teamName))
            }
        )
    }
}
