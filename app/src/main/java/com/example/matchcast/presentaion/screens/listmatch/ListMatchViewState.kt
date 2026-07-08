package com.example.matchcast.presentaion.screens.listmatch

import android.media.Image
import com.example.matchcast.data.local.AppDatabase
import com.example.matchcast.domain.model.Match

sealed class ListMatchViewState{

    data object Loading: ListMatchViewState()

    data class Error(
        val icon: Int,
        val description: String
    ): ListMatchViewState()

    data class Display(
        val listMatch: List<Match> = emptyList()
    ): ListMatchViewState()
}
