package com.example.matchcast.presentaion.navigation

import android.net.Uri
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
import com.example.matchcast.presentaion.screens.standings.StandingsScreen
import com.example.matchcast.presentaion.screens.standings.states.StandingsAction
import com.example.matchcast.presentaion.screens.team.TeamScreen
import com.example.matchcast.presentaion.screens.team.states.TeamAction

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
                        is ListMatchAction.NavigateToTeam -> {
                            navController.navigate(Screen.teamDetailRoute(action.teamName))
                        }
                        is ListMatchAction.NavigateToStandings -> {
                            navController.navigate(Screen.Standings)
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
                        is DetailMatchAction.NavigateToTeam -> {
                            navController.navigate(Screen.teamDetailRoute(action.teamName))
                        }
                    }
                }
            )
        }
        composable(route = Screen.Standings) {
            StandingsScreen(
                onAction = { action ->
                    when(action){
                        is StandingsAction.CloseScreen -> {
                            navController.popBackStack()
                        }
                        is StandingsAction.NavigateToTeam -> {
                            navController.navigate(Screen.teamDetailRoute(action.teamName))
                        }
                    }
                }
            )
        }
        composable(
            route = Screen.TeamDetail,
            arguments = listOf(
                navArgument("teamName") {
                    type = NavType.StringType
                }
            )
        ) {
            backStackEntry ->
            val teamName = backStackEntry.arguments?.getString("teamName")
                ?.let { Uri.decode(it) }
                ?: return@composable

            TeamScreen(
                teamName = teamName,
                onAction = { action ->
                    when(action){
                        is TeamAction.CloseScreen -> {
                            navController.popBackStack()
                        }
                        is TeamAction.NavigateToDetail -> {
                            navController.navigate(Screen.detailMatchRoute(action.matchId))
                        }
                        is TeamAction.NavigateToTeam -> {
                            navController.navigate(Screen.teamDetailRoute(action.teamName))
                        }
                    }
                }
            )
        }
    }
}
