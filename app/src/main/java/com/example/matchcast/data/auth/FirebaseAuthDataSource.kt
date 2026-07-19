package com.example.matchcast.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun authState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun signInAnonymously(): FirebaseUser {
        val result = firebaseAuth.signInAnonymously().await()
        return result.user ?: error("Anonymous sign-in failed")
    }

    suspend fun signInWithEmail(email: String,password: String): FirebaseUser{
        val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
        return result.user ?:error("Email sign-in failed")
    }

    suspend fun signUpWithEmail(email: String,password: String): FirebaseUser{
        val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
        return result.user ?:error("Email sign-up failed")
    }

    suspend fun linkWithGoogleToken(idToken: String): FirebaseUser{
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        val user = firebaseAuth.currentUser ?: error("No current user to link")
        val result = user.linkWithCredential(credential).await()
        return result.user ?: error("Link with Google sign-in failed")
    }

    suspend fun signUpWithGoogleIdToken(idToken: String): FirebaseUser{
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user ?: error("Google sign-in failed")
    }

    fun signOut(){
        firebaseAuth.signOut()
    }
}