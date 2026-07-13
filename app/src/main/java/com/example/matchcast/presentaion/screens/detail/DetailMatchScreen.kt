package com.example.matchcast.presentaion.screens.detail


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchAction
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchEvent
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchState
import com.example.matchcast.presentaion.theme.components.DetailMatchContent
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading


@Composable
fun DetailMatchScreen(
    matchId: Int,
    onAction: (DetailMatchAction) -> Unit,
    viewModel: DetailMatchModel = hiltViewModel()
){

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.viewAction.collect {
            action -> onAction(action)
        }
    }

    LaunchedEffect(matchId) {
        viewModel.obtainEvent(DetailMatchEvent.EnterScreen(matchId))
    }

    when(val state = viewState){
        is DetailMatchState.Loading -> FullScreenLoading()
        is DetailMatchState.Display -> DetailMatchContent(
           match =  state.match,
            onBackCLick = {
                viewModel.obtainEvent(event = DetailMatchEvent.OnBackClick)
            }
        )
        is DetailMatchState.Error -> FullScreenError(
            iconRes = state.icon,
            onRetry = {
                viewModel.obtainEvent(event = DetailMatchEvent.ReloadScreen)
            },
            message = state.description
        )
    }
}
