package com.example.matchcast.presentaion.screens.account.states

sealed class AccountEvent{
    data object EnterScreen: AccountEvent()

    data object OnBackClick: AccountEvent()

    data object OnResetPasswordClick: AccountEvent()

    data object OnSignOutClick: AccountEvent()
}