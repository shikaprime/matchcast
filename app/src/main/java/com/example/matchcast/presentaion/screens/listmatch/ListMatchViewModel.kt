package com.example.matchcast.presentaion.screens.listmatch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.model.Match
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.listmatch.states.FilterType
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchEvent
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchState
import com.example.matchcast.presentaion.screens.listmatch.utils.FilterUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListMatchViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private val _allMatches = MutableStateFlow<List<Match>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)
    private val _activeFilters = MutableStateFlow<Map<FilterType, String>>(emptyMap())

    private val _viewState = MutableStateFlow<ListMatchState>(ListMatchState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<ListMatchAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    private var loadDataJob: Job? = null

    fun obtainEvent(event: ListMatchEvent) {
        when (event) {
            is ListMatchEvent.EnterScreen -> loadData()
            is ListMatchEvent.ReloadScreen -> loadData()
            is ListMatchEvent.OnMatchClick -> sideEffect(ListMatchAction.NavigateToDetail(event.matchId))
            is ListMatchEvent.SearchActivate -> _isSearchActive.value = true
            is ListMatchEvent.SearchQueryChanged -> _searchQuery.value = event.query
            is ListMatchEvent.SearchClear -> {
                _searchQuery.value = ""
                _isSearchActive.value = false
            }
            is ListMatchEvent.FilterOptionSelected -> toggleFilter(event.filterType, event.value)
            is ListMatchEvent.ClearFilters -> _activeFilters.value = emptyMap()
        }
    }

    private fun toggleFilter(filterType: FilterType, value: String) {
        val current = _activeFilters.value
        _activeFilters.value = if (current[filterType] == value) {
            current - filterType
        } else {
            current + (filterType to value)
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            _viewState.value = ListMatchState.Loading
            try {
                repository.refreshMatch()
                combine(
                    repository.getMatches().onEach { _allMatches.value = it },
                    _searchQuery,
                    _isSearchActive,
                    _activeFilters
                ) { matches, query, isSearchActive, filters ->
                    ListMatchState.Display(
                        matches = FilterUtils.applyFilters(matches, filters, query),
                        searchQuery = query,
                        isSearchActive = isSearchActive,
                        availableFilters = FilterUtils.extractAvailableFilters(matches),
                        activeFilters = filters
                    )
                }.collect { displayState ->
                    _viewState.value = displayState
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка загрузки матчей", e)
                _viewState.value = ListMatchState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }

    private fun sideEffect(action: ListMatchAction) {
        actionsChannel.trySend(action)
    }

    private companion object {
        const val TAG = "ListMatchVM"
    }
}
