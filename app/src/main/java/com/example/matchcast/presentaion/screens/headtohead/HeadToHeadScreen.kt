package com.example.matchcast.presentaion.screens.headtohead

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadAction
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadEvent
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.HeadToHeadContent

@Composable
fun HeadToHeadScreen(
    teamA: String,
    teamB: String,
    onAction: (HeadToHeadAction) -> Unit,
    viewModel: HeadToHeadViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(teamA, teamB) {
        viewModel.obtainEvent(HeadToHeadEvent.EnterScreen(teamA, teamB))
    }

    when (val state = viewState) {
        is HeadToHeadState.Loading -> FullScreenLoading()
        is HeadToHeadState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(HeadToHeadEvent.ReloadScreen)
            }
        )
        is HeadToHeadState.Display -> HeadToHeadContent(
            headToHead = state.headToHead,
            onBackClick = {
                viewModel.obtainEvent(HeadToHeadEvent.OnBackClick)
            },
            onMatchClick = { matchId ->
                viewModel.obtainEvent(HeadToHeadEvent.OnMatchClick(matchId))
            }
        )
    }
}
