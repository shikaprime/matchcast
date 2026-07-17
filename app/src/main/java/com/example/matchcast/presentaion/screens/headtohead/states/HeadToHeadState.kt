package com.example.matchcast.presentaion.screens.headtohead.states

import com.example.matchcast.domain.model.HeadToHead

sealed class HeadToHeadState {

    data object Loading : HeadToHeadState()

    data class Error(
        val icon: Int,
        val description: String
    ) : HeadToHeadState()

    data class Display(
        val headToHead: HeadToHead
    ) : HeadToHeadState()
}
