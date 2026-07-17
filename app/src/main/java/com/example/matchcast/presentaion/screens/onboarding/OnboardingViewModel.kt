package com.example.matchcast.presentaion.screens.onboarding

import androidx.lifecycle.ViewModel
import com.example.matchcast.data.local.OnboardingPreferences
import com.example.matchcast.presentaion.screens.onboarding.states.OnboardingAction
import com.example.matchcast.presentaion.screens.onboarding.states.OnboardingEvent
import com.example.matchcast.presentaion.screens.onboarding.states.OnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences
) : ViewModel() {

    private val _viewState = MutableStateFlow<OnboardingState>(OnboardingState.Display)
    val viewState = _viewState.asStateFlow()

    private val actionsChannel = Channel<OnboardingAction>(capacity = Channel.CONFLATED)
    val viewAction = actionsChannel.receiveAsFlow()

    fun obtainEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnSkipClick, is OnboardingEvent.OnFinishClick -> completeOnboarding()
        }
    }

    private fun completeOnboarding() {
        onboardingPreferences.setOnboardingCompleted()
        actionsChannel.trySend(OnboardingAction.NavigateToHome)
    }
}
