package com.example.matchcast.presentaion.screens.headtohead

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadAction
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadEvent
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadToHeadViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private var currentTeamA: String = ""
    private var currentTeamB: String = ""
    private var loadDataJob: Job? = null

    private val _viewState = MutableStateFlow<HeadToHeadState>(HeadToHeadState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<HeadToHeadAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    fun obtainEvent(event: HeadToHeadEvent) {
        when (event) {
            is HeadToHeadEvent.EnterScreen -> loadData(event.teamA, event.teamB)
            is HeadToHeadEvent.ReloadScreen -> loadData(currentTeamA, currentTeamB)
            is HeadToHeadEvent.OnBackClick -> sideEffect(HeadToHeadAction.CloseScreen)
            is HeadToHeadEvent.OnMatchClick -> sideEffect(HeadToHeadAction.NavigateToDetail(event.matchId))
        }
    }

    private fun loadData(teamA: String, teamB: String) {
        currentTeamA = teamA
        currentTeamB = teamB
        loadDataJob?.cancel()

        loadDataJob = viewModelScope.launch {
            _viewState.value = HeadToHeadState.Loading
            try {
                repository.getHeadToHead(teamA, teamB).collect { headToHead ->
                    _viewState.value = HeadToHeadState.Display(headToHead = headToHead)
                }
            } catch (e: Exception) {
                _viewState.value = HeadToHeadState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }

    private fun sideEffect(action: HeadToHeadAction) {
        actionsChannel.trySend(action)
    }
}
