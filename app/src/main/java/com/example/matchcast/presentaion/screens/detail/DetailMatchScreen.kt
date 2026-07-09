package com.example.matchcast.presentaion.screens.detail


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchAction
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchEvent


@Composable
fun DetailMatchState(
    matchId: Int,
    onAction: (DetailMatchAction) -> Unit,
    viewModel: DetailMatchModel = hiltViewModel()
){

    //val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect {
            action -> onAction(action)
        }
    }

    LaunchedEffect(matchId) {
        viewModel.obtainEvent(DetailMatchEvent.EnterScreen(matchId))
    }


}
