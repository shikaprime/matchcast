package com.example.matchcast.presentaion.screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.data.repository.teamFullNameMap
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.team.states.TeamAction
import com.example.matchcast.presentaion.screens.team.states.TeamEvent
import com.example.matchcast.presentaion.screens.team.states.TeamState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private var currentTeamName: String = ""
    private var matchLoadingJob: Job? = null

    private val _viewState = MutableStateFlow<TeamState>(TeamState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<TeamAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    fun obtainEvent(event: TeamEvent) {
        when (event) {
            is TeamEvent.EnterScreen -> loadTeam(event.teamName)
            is TeamEvent.ReloadScreen -> loadTeam(currentTeamName)
            is TeamEvent.OnBackClick -> sideEffect(TeamAction.CloseScreen)
            is TeamEvent.OnMatchClick -> sideEffect(TeamAction.NavigateToDetail(event.matchId))
            is TeamEvent.OnTeamClick -> sideEffect(TeamAction.NavigateToTeam(event.teamName))
        }
    }

    private fun loadTeam(teamName: String) {
        currentTeamName = teamName
        matchLoadingJob?.cancel()

        matchLoadingJob = viewModelScope.launch {
            _viewState.value = TeamState.Loading
            try {
                repository.getTeamMatches(teamName).collect { matches ->
                    if (matches.isEmpty()) {
                        _viewState.value = TeamState.Error(
                            icon = R.drawable.error_svgrepo_com,
                            description = "Матчи команды не найдены"
                        )
                        return@collect
                    }

                    val mostRecent = matches.first()
                    val form = if (mostRecent.homeTeam == teamName) {
                        mostRecent.homeTeamForm
                    } else {
                        mostRecent.awayTeamForm
                    }
                    val stadium = matches.firstOrNull { it.homeTeam == teamName }?.location
                        ?: mostRecent.location

                    _viewState.value = TeamState.Display(
                        teamName = teamName,
                        fullName = teamFullNameMap[teamName] ?: teamName,
                        stadium = stadium,
                        form = form,
                        recentMatches = matches
                    )
                }
            } catch (e: Exception) {
                _viewState.value = TeamState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }

    private fun sideEffect(action: TeamAction) {
        actionsChannel.trySend(action)
    }
}

