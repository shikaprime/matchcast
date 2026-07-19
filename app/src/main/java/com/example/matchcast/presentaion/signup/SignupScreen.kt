package com.example.matchcast.presentaion.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.signup.states.SignupAction
import com.example.matchcast.presentaion.signup.states.SignupEvent
import com.example.matchcast.presentaion.signup.states.SignupState
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading
import com.example.matchcast.presentaion.theme.components.SignupContent

@Composable
fun SignupScreen(
    onAction: (SignupAction) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(SignupEvent.EnterScreen)
    }

    when (val state = viewState) {
        is SignupState.Loading -> FullScreenLoading()
        is SignupState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(SignupEvent.EnterScreen)
            }
        )
        is SignupState.Display -> SignupContent(
            email = state.email,
            password = state.password,
            isPasswordVisible = state.isPasswordVisible,
            isSubmitting = state.isSubmitting,
            emailError = state.emailError,
            passwordError = state.passwordError,
            errorMessage = state.errorMessage,
            onBackClick = {
                viewModel.obtainEvent(SignupEvent.OnBackClick)
            },
            onEmailChange = { email ->
                viewModel.obtainEvent(SignupEvent.EmailChanged(email))
            },
            onPasswordChange = { password ->
                viewModel.obtainEvent(SignupEvent.PasswordChanged(password))
            },
            onTogglePasswordVisibility = {
                viewModel.obtainEvent(SignupEvent.TogglePasswordVisibility)
            },
            onSignUpClick = {
                viewModel.obtainEvent(SignupEvent.OnSignUpClick)
            },
            onGoToLoginClick = {
                viewModel.obtainEvent(SignupEvent.OnGoToLoginClick)
            }
        )
    }
}
