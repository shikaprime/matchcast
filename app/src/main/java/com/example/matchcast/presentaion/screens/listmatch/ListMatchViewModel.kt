package com.example.matchcast.presentaion.screens.listmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchEvent
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListMatchViewModel @Inject constructor(
    private val repository: MatchRepository
): ViewModel() {

    private var allMatches: List<Match> = emptyList()
    private var loadDataJob: Job? = null

    private val _viewState = MutableStateFlow<ListMatchState>(ListMatchState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<ListMatchAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    fun obtainEvent(event: ListMatchEvent) {
        when (val state = _viewState.value) {
            is ListMatchState.Loading -> reduce(state, event)
            is ListMatchState.Display -> reduce(state, event)
            is ListMatchState.Error -> reduce(state, event)
            is ListMatchState.Search -> reduce(state, event)
        }
    }

    private fun reduce(state: ListMatchState.Loading, event: ListMatchEvent) {
        when(event){
            is ListMatchEvent.EnterScreen -> loadData()
            else -> {}
        }
    }

    private fun reduce(state: ListMatchState.Display, event: ListMatchEvent){
        when(event){
            is ListMatchEvent.OnMatchClick -> {
                sideEffect(ListMatchAction.NavigateToDetail(event.matchId))
            }
            is ListMatchEvent.SearchQueryChanged -> search(event.query)
            else -> {}
        }
    }

    private fun reduce(state: ListMatchState.Search, event: ListMatchEvent){
        when(event){
            is ListMatchEvent.SearchQueryChanged -> search(event.query)
            is ListMatchEvent.SearchClear -> {
                _viewState.value = ListMatchState.Display(allMatches)
            }
            is ListMatchEvent.OnMatchClick -> sideEffect(ListMatchAction.NavigateToDetail(event.matchId))
            else -> {}
        }
    }

    private fun reduce(state: ListMatchState.Error, event: ListMatchEvent){
        when(event){
            is ListMatchEvent.ReloadScreen -> loadData()
            else -> {}
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            try {
                repository.refreshMatch()
                repository.getMatches().collect { matches: List<Match> ->
                    allMatches = matches
                    val currentState = _viewState.value
                    _viewState.value = when (currentState) {
                        is ListMatchState.Search -> {
                            currentState.copy(
                                results = filterMatches(currentState.query, matches),
                                isLoading = false
                            )
                        }
                        else -> ListMatchState.Display(allMatches)
                    }
                }
            } catch (e: Exception) {
                _viewState.value = ListMatchState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }

    private fun sideEffect(action: ListMatchAction){
        actionsChannel.trySend(action)
    }

    private fun search(query: String){
        val filtered = filterMatches(query, allMatches)
        _viewState.value = ListMatchState.Search(
            query = query,
            results = filtered,
            isLoading = false
        )
    }

    private fun filterMatches(query: String, matches: List<Match>): List<Match> {
        return if (query.isBlank()) matches
        else matches.filter { match ->
            match.homeTeam.contains(query, ignoreCase = true) ||
                    match.awayTeam.contains(query, ignoreCase = true)
        }
    }
}