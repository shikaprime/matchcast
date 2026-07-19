package com.example.matchcast.presentaion.signup.states

sealed class SignupState{
    data object Loading: SignupState()

    data class Error(
        val icon: Int,
        val description: String
    ): SignupState()

    data class Display(
        val email: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
        val isSubmitting: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val errorMessage: String? = null
    ): SignupState()
}