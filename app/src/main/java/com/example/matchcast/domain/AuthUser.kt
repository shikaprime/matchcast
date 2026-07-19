package com.example.matchcast.domain

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String? = null,
    val isAnonymous: Boolean
)
