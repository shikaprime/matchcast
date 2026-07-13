package com.example.matchcast.presentaion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.matchcast.presentaion.navigation.MatchCastNavGraph
import com.example.matchcast.presentaion.theme.MatchCastTheme


class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatchCastTheme {
                MatchCastNavGraph()
            }
        }
    }
}