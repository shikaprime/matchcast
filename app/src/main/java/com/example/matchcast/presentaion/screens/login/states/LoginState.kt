package com.example.matchcast.presentaion.screens.login.states

sealed class LoginState{
    data object Loading: LoginState()

    data class Error(
        val icon: Int,
        val description: String
    ): LoginState()

    data class Display(
        val email: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
        val isSubmitting: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val errorMessage: String? = null
    ): LoginState()
}
