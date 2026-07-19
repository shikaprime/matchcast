package com.example.matchcast.presentaion.screens.login.states

sealed class LoginEvent {
    data object EnterScreen : LoginEvent()

    data class EmailChanged(val email: String) : LoginEvent()

    data class PasswordChanged(val password: String) : LoginEvent()

    data object TogglePasswordVisibility : LoginEvent()

    data object OnLoginClick : LoginEvent()

    data object OnSignUpClick : LoginEvent()

    data object OnBackClick : LoginEvent()
    data object OnSkipClick : LoginEvent()
}