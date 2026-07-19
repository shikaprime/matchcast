package com.example.matchcast.presentaion.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.AuthRepository
import com.example.matchcast.presentaion.screens.login.states.LoginAction
import com.example.matchcast.presentaion.screens.login.states.LoginEvent
import com.example.matchcast.presentaion.screens.login.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginState>(LoginState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionChannel = Channel<LoginAction>(capacity = Channel.CONFLATED)
    val viewAction = actionChannel.receiveAsFlow()

    private var loadDataJob: Job? = null

    fun obtainEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnterScreen -> loadData()
            is LoginEvent.EmailChanged -> updateDisplay {
                it.copy(email = event.email, emailError = null, errorMessage = null)
            }
            is LoginEvent.PasswordChanged -> updateDisplay {
                it.copy(password = event.password, passwordError = null, errorMessage = null)
            }
            is LoginEvent.TogglePasswordVisibility -> updateDisplay {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            is LoginEvent.OnLoginClick -> signIn()
            is LoginEvent.OnSignUpClick -> sideEffect(LoginAction.NavigateToSignUp)
            is LoginEvent.OnBackClick -> sideEffect(LoginAction.CloseScreen)
            is LoginEvent.OnSkipClick -> signInAsGuest()
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            _viewState.value = LoginState.Loading
            try {
                repository.authState().collect { user ->
                    if (user != null) {
                        sideEffect(LoginAction.NavigateToHome)
                    } else {
                        val current = _viewState.value
                        if (current !is LoginState.Display) {
                            _viewState.value = LoginState.Display()
                        }
                    }
                }
            } catch (e: Exception) {
                _viewState.value = LoginState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Account loading error"
                )
            }
        }
    }

    private fun signIn() {
        val state = _viewState.value as? LoginState.Display ?: return
        if (state.isSubmitting) return

        val email = state.email.trim()
        val password = state.password
        val emailError = if (email.isBlank()) "Enter email" else null
        val passwordError = if (password.isBlank()) "Enter password" else null

        if (emailError != null || passwordError != null) {
            _viewState.value = state.copy(
                emailError = emailError,
                passwordError = passwordError,
                errorMessage = null
            )
            return
        }

        viewModelScope.launch {
            _viewState.value = state.copy(
                isSubmitting = true,
                emailError = null,
                passwordError = null,
                errorMessage = null
            )
            try {
                repository.signIn(email, password)
                sideEffect(LoginAction.NavigateToHome)
            } catch (e: Exception) {
                _viewState.value = state.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Email sign-in failed"
                )
            }
        }
    }

    private fun signInAsGuest() {
        val state = _viewState.value as? LoginState.Display ?: LoginState.Display()
        if (state.isSubmitting) return

        viewModelScope.launch {
            _viewState.value = state.copy(isSubmitting = true, errorMessage = null)
            try {
                repository.signInAsGuest()
                sideEffect(LoginAction.NavigateToHome)
            } catch (e: Exception) {
                _viewState.value = state.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Guest sign-in failed"
                )
            }
        }
    }

    private fun updateDisplay(transform: (LoginState.Display) -> LoginState.Display) {
        val current = _viewState.value as? LoginState.Display ?: return
        _viewState.value = transform(current)
    }

    private fun sideEffect(action: LoginAction) {
        actionChannel.trySend(action)
    }
}
