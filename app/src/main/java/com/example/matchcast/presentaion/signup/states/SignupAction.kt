package com.example.matchcast.presentaion.signup.states

sealed class SignupAction {
    data object NavigateToHome : SignupAction()

    data object NavigateToLogin : SignupAction()

    data object CloseScreen : SignupAction()

    data class ShowMessage(val message: String) : SignupAction()
}
