package com.example.matchcast.presentaion.screens.login.states

sealed class LoginAction {
    data object NavigateToHome : LoginAction()

    data object NavigateToSignUp : LoginAction()

    data object CloseScreen : LoginAction()

    data class ShowMessage(val message: String) : LoginAction()
}