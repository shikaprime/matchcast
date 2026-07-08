package com.example.matchcast.presentaion.screens.listmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchEvent
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListMatchViewModel(
    val repository: MatchRepository
): ViewModel() {

    private var allMatches: List<Match> = emptyList()

    private val _viewState = MutableStateFlow<ListMatchViewState>(ListMatchViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<ListMatchAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    fun obtainEvent(event: ListMatchEvent) {
        when (val state = _viewState.value) {
            is ListMatchViewState.Loading -> reduce(state,event)
            is ListMatchViewState.Display ->reduce(state,event)
            is ListMatchViewState.Error -> reduce(state,event)
            is ListMatchViewState.Search -> reduce(state,event)
        }
    }

    private fun reduce(state: ListMatchViewState.Loading, event: ListMatchEvent) {
        when(event){
            is ListMatchEvent.EnterScreen -> loadData()
            else -> {}
        }
    }

    private fun reduce(state: ListMatchViewState.Display,event: ListMatchEvent){
        when(event){
            is ListMatchEvent.OnMatchClick ->{
                sideEffect(ListMatchAction.NavigateToDetail(event.matchId))
            }
            is ListMatchEvent.SearchQueryChanged -> search(event.query)
            else -> {}
        }
    }

    private fun reduce(state: ListMatchViewState.Search, event: ListMatchEvent){
        when(event){
            is ListMatchEvent.SearchQueryChanged -> search(event.query)
            is ListMatchEvent.SearchClear -> {
                _viewState.value = ListMatchViewState.Display(allMatches)
            }
            is ListMatchEvent.OnMatchClick -> sideEffect(ListMatchAction.NavigateToDetail(event.matchId))
            else -> {}
        }
    }
    private fun reduce(state: ListMatchViewState.Error, event: ListMatchEvent){
        when(event){
            is ListMatchEvent.ReloadScreen -> loadData()
            else -> {}
        }
    }
    private fun loadData() {
        viewModelScope.launch {
            try {
                repository.refreshMatch()
                repository.getMatches().collect { matches: List<Match> ->
                    allMatches = matches
                    val currentState = _viewState.value
                    _viewState.value = when (currentState) {
                        is ListMatchViewState.Search -> {
                            currentState.copy(
                                results = filterMatches(currentState.query, matches),
                                isLoading = false
                            )
                        }
                        else -> ListMatchViewState.Display(allMatches)
                    }
                }
            } catch (e: Exception) {
                _viewState.value = ListMatchViewState.Error(
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
        if (query.isBlank()) {
            _viewState.value = ListMatchViewState.Display(allMatches)
            return
        }

        viewModelScope.launch {
            _viewState.value = ListMatchViewState.Search(
                query = query,
                results = emptyList(),
                isLoading = true
            )
            val filtered = filterMatches(query,allMatches)
            _viewState.value = ListMatchViewState.Search(
                query = query,
                results = filtered,
                isLoading = false
            )
        }
    }

    private fun filterMatches(query: String, matches: List<Match>): List<Match> {
        return if (query.isBlank()) matches
        else matches.filter { match ->
            match.homeTeam.contains(query, ignoreCase = true) ||
                    match.awayTeam.contains(query, ignoreCase = true)
        }
    }
}
