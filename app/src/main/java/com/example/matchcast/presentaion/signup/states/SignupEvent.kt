package com.example.matchcast.presentaion.signup.states

sealed class SignupEvent {
    data object EnterScreen : SignupEvent()

    data class EmailChanged(val email: String) : SignupEvent()

    data class PasswordChanged(val password: String) : SignupEvent()

    data object TogglePasswordVisibility : SignupEvent()

    data object OnSignUpClick : SignupEvent()

    data object OnGoToLoginClick : SignupEvent()

    data object OnBackClick : SignupEvent()
}
