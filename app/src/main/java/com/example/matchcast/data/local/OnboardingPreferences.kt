package com.example.matchcast.data.local

import android.content.Context
import androidx.core.content.edit

class OnboardingPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun hasCompletedOnboarding(): Boolean = prefs.getBoolean(KEY_COMPLETED, false)

    fun setOnboardingCompleted() {
        prefs.edit { putBoolean(KEY_COMPLETED, true) }
    }

    companion object {
        const val SHOW_ONBOARDING_EVERY_LAUNCH = true

        private const val PREFS_NAME = "matchcast_onboarding_prefs"
        private const val KEY_COMPLETED = "onboarding_completed"
    }
}
