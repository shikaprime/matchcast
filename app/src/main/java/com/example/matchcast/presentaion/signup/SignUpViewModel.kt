package com.example.matchcast.presentaion.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchcast.R
import com.example.matchcast.domain.repository.AuthRepository
import com.example.matchcast.presentaion.signup.states.SignupAction
import com.example.matchcast.presentaion.signup.states.SignupEvent
import com.example.matchcast.presentaion.signup.states.SignupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<SignupState>(SignupState.Loading)
    val viewState = _viewState.asStateFlow()

    private val actionChannel = Channel<SignupAction>(capacity = Channel.CONFLATED)
    val viewAction = actionChannel.receiveAsFlow()

    private var loadDataJob: Job? = null

    fun obtainEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.EnterScreen -> loadData()
            is SignupEvent.EmailChanged -> updateDisplay {
                it.copy(email = event.email, emailError = null, errorMessage = null)
            }
            is SignupEvent.PasswordChanged -> updateDisplay {
                it.copy(password = event.password, passwordError = null, errorMessage = null)
            }
            is SignupEvent.TogglePasswordVisibility -> updateDisplay {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            is SignupEvent.OnSignUpClick -> signUp()
            is SignupEvent.OnGoToLoginClick -> sideEffect(SignupAction.NavigateToLogin)
            is SignupEvent.OnBackClick -> sideEffect(SignupAction.CloseScreen)
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            _viewState.value = SignupState.Loading
            try {
                repository.authState().collect { user ->
                    if (user != null) {
                        sideEffect(SignupAction.NavigateToHome)
                    } else {
                        val current = _viewState.value
                        if (current !is SignupState.Display) {
                            _viewState.value = SignupState.Display()
                        }
                    }
                }
            } catch (e: Exception) {
                _viewState.value = SignupState.Error(
                    icon = R.drawable.error_svgrepo_com,
                    description = e.message ?: "Loading error"
                )
            }
        }
    }

    private fun signUp() {
        val state = _viewState.value as? SignupState.Display ?: return
        if (state.isSubmitting) return

        val email = state.email.trim()
        val password = state.password
        val emailError = if (email.isBlank()) "Enter email" else null
        val passwordError = when {
            password.isBlank() -> "Enter password"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }

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
                repository.signUp(email, password)
                sideEffect(SignupAction.NavigateToHome)
            } catch (e: Exception) {
                _viewState.value = state.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Sign-up failed"
                )
            }
        }
    }

    private fun updateDisplay(transform: (SignupState.Display) -> SignupState.Display) {
        val current = _viewState.value as? SignupState.Display ?: return
        _viewState.value = transform(current)
    }

    private fun sideEffect(action: SignupAction) {
        actionChannel.trySend(action)
    }
}
