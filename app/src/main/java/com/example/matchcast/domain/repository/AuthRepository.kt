package com.example.matchcast.domain.repository

import com.example.matchcast.domain.model.Account
import com.example.matchcast.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun authState(): Flow<AuthUser?>

    fun currentUser(): AuthUser?

    suspend fun signIn(email: String, password: String): AuthUser

    suspend fun signUp(email: String, password: String): AuthUser

    suspend fun signInAsGuest(): AuthUser

    suspend fun signOut()

    suspend fun linkWithGoogle(idToken: String): AuthUser

    suspend fun signInWithGoogle(idToken: String): AuthUser

    fun getAccount(): Account
}