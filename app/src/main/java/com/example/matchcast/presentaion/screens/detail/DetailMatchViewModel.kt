package com.example.matchcast.presentaion.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchAction
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchEvent
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMatchViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private var currentMatchId: Int = -1
    private var matchLoadingJob: Job? = null

    private val _viewState = MutableStateFlow<DetailMatchState>(DetailMatchState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<DetailMatchAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    private fun sideEffect(action: DetailMatchAction) {
        actionsChannel.trySend(action)
    }

    fun obtainEvent(event: DetailMatchEvent) {
        when (val state = _viewState.value) {
            is DetailMatchState.Loading -> reduce(state, event)
            is DetailMatchState.Display -> reduce(state, event)
            is DetailMatchState.Error -> reduce(state, event)
        }
    }

    private fun reduce(state: DetailMatchState.Loading, event: DetailMatchEvent) {
        when (event) {
            is DetailMatchEvent.EnterScreen -> loadMatch(event.matchId)
            else -> {}
        }
    }

    private fun reduce(state: DetailMatchState.Error, event: DetailMatchEvent) {
        when (event) {
            is DetailMatchEvent.ReloadScreen -> loadMatch(currentMatchId)
            is DetailMatchEvent.OnBackClick -> sideEffect(DetailMatchAction.CloseScreen)
            else -> {}
        }
    }

    private fun reduce(state: DetailMatchState.Display, event: DetailMatchEvent) {
        when (event) {
            is DetailMatchEvent.OnBackClick -> sideEffect(DetailMatchAction.CloseScreen)
            else -> {}
        }
    }

    private fun loadMatch(id: Int) {
        currentMatchId = id
        matchLoadingJob?.cancel()

        matchLoadingJob = viewModelScope.launch {
            _viewState.value = DetailMatchState.Loading
            try {
                repository.getMatch(id).collect { match ->
                    _viewState.value = DetailMatchState.Display(match)
                }
            } catch (e: Exception) {
                _viewState.value = DetailMatchState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }
}
