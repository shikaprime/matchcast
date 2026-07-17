package com.example.matchcast.presentaion.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesAction
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesEvent
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<FavoritesAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    private var loadDataJob: Job? = null

    fun obtainEvent(event: FavoritesEvent) {
        when (event) {
            is FavoritesEvent.EnterScreen -> loadData()
            is FavoritesEvent.ReloadScreen -> loadData()
            is FavoritesEvent.OnBackClick -> sideEffect(FavoritesAction.CloseScreen)
            is FavoritesEvent.OnTeamClick -> sideEffect(FavoritesAction.NavigateToTeam(event.teamName))
            is FavoritesEvent.OnRemoveClick -> removeFavorite(event.teamName)
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            _viewState.value = FavoritesState.Loading
            try {
                repository.getFavoriteTeams().collect { favorites ->
                    _viewState.value = FavoritesState.Display(favorites = favorites)
                }
            } catch (e: Exception) {
                _viewState.value = FavoritesState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }

    private fun removeFavorite(teamName: String) {
        viewModelScope.launch {
            repository.toggleFavoriteTeam(teamName)
        }
    }

    private fun sideEffect(action: FavoritesAction) {
        actionsChannel.trySend(action)
    }
}
