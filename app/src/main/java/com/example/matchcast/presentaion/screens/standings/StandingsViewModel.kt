package com.example.matchcast.presentaion.screens.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.standings.states.StandingsAction
import com.example.matchcast.presentaion.screens.standings.states.StandingsEvent
import com.example.matchcast.presentaion.screens.standings.states.StandingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StandingsViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<StandingsState>(StandingsState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<StandingsAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    private var loadDataJob: Job? = null

    fun obtainEvent(event: StandingsEvent) {
        when (event) {
            is StandingsEvent.EnterScreen -> loadData()
            is StandingsEvent.ReloadScreen -> loadData()
            is StandingsEvent.OnBackClick -> sideEffect(StandingsAction.CloseScreen)
            is StandingsEvent.OnTeamClick -> sideEffect(StandingsAction.NavigateToTeam(event.teamName))
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            _viewState.value = StandingsState.Loading
            try {
                repository.refreshMatch()
                combine(
                    repository.getStandings(),
                    repository.getFavoriteTeams()
                ) { standings, favorites -> standings to favorites }
                    .collect { (standings, favorites) ->
                        _viewState.value = StandingsState.Display(
                            standings = standings,
                            favoriteTeamNames = favorites.map { it.teamName }.toSet()
                        )
                    }
            } catch (e: Exception) {
                _viewState.value = StandingsState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }

    private fun sideEffect(action: StandingsAction) {
        actionsChannel.trySend(action)
    }
}
