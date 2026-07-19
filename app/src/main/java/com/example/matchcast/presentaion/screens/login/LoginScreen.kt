package com.example.matchcast.presentaion.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.login.states.LoginAction
import com.example.matchcast.presentaion.screens.login.states.LoginEvent
import com.example.matchcast.presentaion.screens.login.states.LoginState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.LoginContent

@Composable
fun LoginScreen(
    onAction: (LoginAction) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(LoginEvent.EnterScreen)
    }

    when (val state = viewState) {
        is LoginState.Loading -> FullScreenLoading()
        is LoginState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(LoginEvent.EnterScreen)
            }
        )
        is LoginState.Display -> LoginContent(
            state = state,
            onBackClick = {
                viewModel.obtainEvent(LoginEvent.OnBackClick)
            },
            onEmailChange = { email ->
                viewModel.obtainEvent(LoginEvent.EmailChanged(email))
            },
            onPasswordChange = { password ->
                viewModel.obtainEvent(LoginEvent.PasswordChanged(password))
            },
            onTogglePasswordVisibility = {
                viewModel.obtainEvent(LoginEvent.TogglePasswordVisibility)
            },
            onLoginClick = {
                viewModel.obtainEvent(LoginEvent.OnLoginClick)
            },
            onSignUpClick = {
                viewModel.obtainEvent(LoginEvent.OnSignUpClick)
            },
            onSkipClick = {
                viewModel.obtainEvent(LoginEvent.OnSkipClick)
            }
        )
    }
}
