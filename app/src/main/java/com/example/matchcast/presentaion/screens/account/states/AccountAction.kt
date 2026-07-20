package com.example.matchcast.presentaion.screens.account.states

sealed class AccountAction {
    data object CloseScreen : AccountAction()

    data object NavigateToResetPassword : AccountAction()

    data object NavigateToLogin : AccountAction()
}
