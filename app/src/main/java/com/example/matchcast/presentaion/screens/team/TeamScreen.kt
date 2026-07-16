package com.example.matchcast.presentaion.screens.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.team.states.TeamAction
import com.example.matchcast.presentaion.screens.team.states.TeamEvent
import com.example.matchcast.presentaion.screens.team.states.TeamState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.TeamContent

@Composable
fun TeamScreen(
    teamName: String,
    onAction: (TeamAction) -> Unit,
    viewModel: TeamViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(teamName) {
        viewModel.obtainEvent(TeamEvent.EnterScreen(teamName))
    }

    when (val state = viewState) {
        is TeamState.Loading -> FullScreenLoading()
        is TeamState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(TeamEvent.ReloadScreen)
            }
        )
        is TeamState.Display -> TeamContent(
            state = state,
            onBackClick = {
                viewModel.obtainEvent(TeamEvent.OnBackClick)
            },
            onMatchClick = { matchId ->
                viewModel.obtainEvent(TeamEvent.OnMatchClick(matchId))
            },
            onTeamClick = { teamName ->
                viewModel.obtainEvent(TeamEvent.OnTeamClick(teamName))
            } 
        )
    }
}
