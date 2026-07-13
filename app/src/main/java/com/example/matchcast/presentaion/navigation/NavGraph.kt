package com.example.matchcast.presentaion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.matchcast.presentaion.screens.Screen
import com.example.matchcast.presentaion.screens.detail.DetailMatchScreen
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchAction
import com.example.matchcast.presentaion.screens.listmatch.ListMatchScreen
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction

@Composable
fun MatchCastNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = Screen.ListMatch,
        modifier = modifier
    ){
        composable (route = Screen.ListMatch) {
            ListMatchScreen(
                onAction = { action ->
                    when(action){
                        is ListMatchAction.NavigateToDetail -> {
                            navController.navigate(Screen.detailMatchRoute(action.matchId))
                        }
                        is ListMatchAction.CloseScreen -> {}
                    }
                }
            )
        }
        composable(
            route = Screen.DetailMatch,
            arguments = listOf(
                navArgument("matchId"){
                    type = NavType.IntType
                }
            )
        ) {
            backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: return@composable

            DetailMatchScreen(
                matchId = matchId,
                onAction = { action ->
                    when(action){
                        is DetailMatchAction.CloseScreen -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}