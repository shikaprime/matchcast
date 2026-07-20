package com.example.matchcast.presentaion.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.AuthRepository
import com.example.matchcast.presentaion.screens.account.states.AccountAction
import com.example.matchcast.presentaion.screens.account.states.AccountEvent
import com.example.matchcast.presentaion.screens.account.states.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<AccountState>(AccountState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionChannel = Channel<AccountAction>(capacity = Channel.CONFLATED)
    val viewAction = actionChannel.receiveAsFlow()

    private var currentLoadJob: Job? = null

    fun obtainEvent(event: AccountEvent) {
        when (event) {
            is AccountEvent.EnterScreen -> loadData()
            is AccountEvent.OnBackClick -> sideEffect(AccountAction.CloseScreen)
            is AccountEvent.OnSignOutClick -> signOut()
            is AccountEvent.OnResetPasswordClick -> sideEffect(AccountAction.NavigateToResetPassword)
        }
    }

    private fun sideEffect(action: AccountAction) {
        actionChannel.trySend(action)
    }

    private fun loadData() {
        currentLoadJob?.cancel()
        currentLoadJob = viewModelScope.launch {
            _viewState.value = AccountState.Loading
            try {
                if (repository.currentUser() == null) {
                    sideEffect(AccountAction.NavigateToLogin)
                    return@launch
                }
                val account = repository.getAccount()
                _viewState.value = AccountState.Display(
                    name = account.name,
                    email = account.email,
                    photo = account.photo
                )
            } catch (e: Exception) {
                _viewState.value = AccountState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Account loading error"
                )
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            sideEffect(AccountAction.NavigateToLogin)
        }
    }
}
