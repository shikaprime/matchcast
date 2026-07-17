package com.example.matchcast.presentaion.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesAction
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesEvent
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesState
import com.example.matchcast.presentaion.theme.components.FavoritesContent
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading

@Composable
fun FavoritesScreen(
    onAction: (FavoritesAction) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(FavoritesEvent.EnterScreen)
    }

    when (val state = viewState) {
        is FavoritesState.Loading -> FullScreenLoading()
        is FavoritesState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(FavoritesEvent.ReloadScreen)
            }
        )
        is FavoritesState.Display -> FavoritesContent(
            favorites = state.favorites,
            onBackClick = {
                viewModel.obtainEvent(FavoritesEvent.OnBackClick)
            },
            onTeamClick = { teamName ->
                viewModel.obtainEvent(FavoritesEvent.OnTeamClick(teamName))
            },
            onRemoveClick = { teamName ->
                viewModel.obtainEvent(FavoritesEvent.OnRemoveClick(teamName))
            }
        )
    }
}
