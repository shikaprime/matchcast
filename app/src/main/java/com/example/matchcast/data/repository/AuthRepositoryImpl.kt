package com.example.matchcast.data.repository

import com.example.matchcast.data.auth.FirebaseAuthDataSource
import com.example.matchcast.domain.AuthUser
import com.example.matchcast.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
     private val firebase: FirebaseAuthDataSource
): AuthRepository {


     override fun authState(): Flow<AuthUser?> {
           return firebase.authState().map { auth ->
                auth?.toDomain()
          }
     }

     override fun currentUser(): AuthUser? {
          return firebase.currentUser?.toDomain()
     }

     override suspend fun signIn(
          email: String,
          password: String
     ): AuthUser {
          return firebase.signInWithEmail(
               email = email,
               password = password
          ).toDomain()
     }

     override suspend fun signUp(
          email: String,
          password: String
     ): AuthUser {
          return firebase.signUpWithEmail(
               email = email,
               password = password
          ).toDomain()
     }

     override suspend fun signInAsGuest(): AuthUser {
          return firebase.signInAnonymously().toDomain()
     }

     override suspend fun signOut(){
          firebase.signOut()
     }

     override suspend fun linkWithGoogle(idToken: String): AuthUser {
          return firebase.linkWithGoogleToken(idToken).toDomain()
     }

     override suspend fun signInWithGoogle(idToken: String): AuthUser {
          return firebase.signUpWithGoogleIdToken(idToken).toDomain()
     }

     fun FirebaseUser.toDomain(): AuthUser{
          return AuthUser(
               uid = this.uid,
               email = this.email,
               displayName = this.displayName,
               isAnonymous = this.isAnonymous
          )
     }
}