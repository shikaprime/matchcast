package com.example.matchcast.presentaion.screens.account.states

import android.net.Uri

sealed class AccountState{
    data object Loading: AccountState()

    data class Error(
        val icon: Int,
        val description: String
    ): AccountState()

    data class Display(
        val email: String?,
        val name: String?,
        val photo: Uri?
    ): AccountState()
}