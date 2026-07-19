package com.example.matchcast.presentaion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.matchcast.domain.repository.AuthRepository
import com.example.matchcast.presentaion.navigation.MatchCastNavGraph
import com.example.matchcast.presentaion.theme.MatchCastTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Temporary smoke-test for AuthRepository — check Logcat tag "AuthTest"
        // Remove this call when login UI is ready.
        testAuthRepository()

        setContent {
            MatchCastTheme {
                MatchCastNavGraph()
            }
        }
    }

    private fun testAuthRepository() {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "currentUser before: ${authRepository.currentUser()}")

                val guest = authRepository.signInAsGuest()
                Log.d(
                    TAG,
                    "guest ok: uid=${guest.uid}, email=${guest.email}, anonymous=${guest.isAnonymous}"
                )

                val fromState = authRepository.authState().first()
                Log.d(TAG, "authState after guest: $fromState")

                // Optional: create a throwaway account (needs Email/Password enabled in Firebase Console)
                // Uncomment and change email if you want to test signUp/signIn:
                //
                // val email = "test_${System.currentTimeMillis()}@example.com"
                // val password = "Test1234!"
                // val created = authRepository.signUp(email, password)
                // Log.d(TAG, "signUp ok: $created")
                // authRepository.signOut()
                // val signedIn = authRepository.signIn(email, password)
                // Log.d(TAG, "signIn ok: $signedIn")

                authRepository.signOut()
                Log.d(TAG, "currentUser after signOut: ${authRepository.currentUser()}")
                Log.d(TAG, "AuthRepository smoke-test finished OK")
            } catch (e: Exception) {
                Log.e(TAG, "AuthRepository smoke-test failed", e)
            }
        }
    }

    private companion object {
        const val TAG = "AuthTest"
    }
}


