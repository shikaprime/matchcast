package com.example.matchcast.presentaion.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.MatchRepository
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchAction
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchEvent
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    val repository: MatchRepository
) : ViewModel(){

    private var currentMatchId: Int = -1
    private val _viewState = MutableStateFlow<DetailMatchViewState>(DetailMatchViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<DetailMatchAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    private fun sideEffect(action: DetailMatchAction){
        actionsChannel.trySend(action)
    }

    fun obtainEvent(event: DetailMatchEvent){
        when(val state = _viewState.value){
            is DetailMatchViewState.Loading -> reduce(state,event)
            is DetailMatchViewState.Display -> reduce(state,event)
            is DetailMatchViewState.Error -> reduce(state,event)
        }
    }

    private fun reduce(state: DetailMatchViewState.Loading, event: DetailMatchEvent){
        when(event){
            is DetailMatchEvent.EnterScreen -> loadMatch(event.matchId)
            else -> {}
        }
    }

    private  fun reduce(state: DetailMatchViewState.Error, event: DetailMatchEvent){
        when(event){
            is DetailMatchEvent.ReloadScreen -> loadMatch(currentMatchId)
            is DetailMatchEvent.OnBackClick -> sideEffect(DetailMatchAction.CloseScreen)
            else -> {}
        }
    }

    private fun reduce(state: DetailMatchViewState.Display, event: DetailMatchEvent){
        when(event){
            is DetailMatchEvent.OnBackClick -> sideEffect(DetailMatchAction.CloseScreen)
            else -> {}
        }
    }

    private fun loadMatch(id: Int){
        currentMatchId = id
        viewModelScope.launch{
            _viewState.value = DetailMatchViewState.Loading
            try {
                repository.getMatch(id).collect { match ->
                    _viewState.value = DetailMatchViewState.Display(match)
                }
            }catch (e: Exception){
                _viewState.value = DetailMatchViewState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Ошибка"
                )
            }
        }
    }
}