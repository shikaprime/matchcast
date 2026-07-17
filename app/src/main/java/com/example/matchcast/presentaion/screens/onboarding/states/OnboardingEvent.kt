package com.example.matchcast.presentaion.screens.onboarding.states

sealed class OnboardingEvent {
    data object OnSkipClick : OnboardingEvent()

    data object OnFinishClick : OnboardingEvent()
}
