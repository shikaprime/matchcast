package com.example.matchcast.presentaion.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.matchcast.data.local.OnboardingPreferences
import com.example.matchcast.presentaion.screens.Screen
import com.example.matchcast.presentaion.screens.about.AboutScreen
import com.example.matchcast.presentaion.screens.detail.DetailMatchScreen
import com.example.matchcast.presentaion.screens.detail.states.DetailMatchAction
import com.example.matchcast.presentaion.screens.favorites.FavoritesScreen
import com.example.matchcast.presentaion.screens.favorites.states.FavoritesAction
import com.example.matchcast.presentaion.screens.headtohead.HeadToHeadScreen
import com.example.matchcast.presentaion.screens.headtohead.states.HeadToHeadAction
import com.example.matchcast.presentaion.screens.listmatch.ListMatchScreen
import com.example.matchcast.presentaion.screens.listmatch.states.ListMatchAction
import com.example.matchcast.presentaion.screens.onboarding.OnboardingScreen
import com.example.matchcast.presentaion.screens.onboarding.states.OnboardingAction
import com.example.matchcast.presentaion.screens.standings.StandingsScreen
import com.example.matchcast.presentaion.screens.standings.states.StandingsAction
import com.example.matchcast.presentaion.screens.team.TeamScreen
import com.example.matchcast.presentaion.screens.team.states.TeamAction

@Composable
fun MatchCastNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val context = LocalContext.current
    val startDestination = remember {
        if (OnboardingPreferences.SHOW_ONBOARDING_EVERY_LAUNCH) {
            Screen.Onboarding
        } else {
            val onboardingPreferences = OnboardingPreferences(context)
            if (onboardingPreferences.hasCompletedOnboarding()) Screen.ListMatch else Screen.Onboarding
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(route = Screen.Onboarding) {
            OnboardingScreen(
                onAction = { action ->
                    when (action) {
                        is OnboardingAction.NavigateToHome -> {
                            navController.navigate(Screen.ListMatch) {
                                popUpTo(Screen.Onboarding) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
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
                        is ListMatchAction.NavigateToFavorites -> {
                            navController.navigate(Screen.Favorites)
                        }
                        is ListMatchAction.NavigateToAbout -> {
                            navController.navigate(Screen.About)
                        }
                        is ListMatchAction.CloseScreen -> {}
                    }
                }
            )
        }
        composable(route = Screen.Favorites) {
            FavoritesScreen(
                onAction = { action ->
                    when (action) {
                        is FavoritesAction.CloseScreen -> {
                            navController.popBackStack()
                        }
                        is FavoritesAction.NavigateToTeam -> {
                            navController.navigate(Screen.teamDetailRoute(action.teamName))
                        }
                    }
                }
            )
        }
        composable(route = Screen.About) {
            AboutScreen(
                onBackClick = {
                    navController.popBackStack()
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
                        is TeamAction.NavigateToHeadToHead -> {
                            navController.navigate(Screen.headToHeadRoute(action.teamA, action.teamB))
                        }
                    }
                }
            )
        }
        composable(
            route = Screen.HeadToHead,
            arguments = listOf(
                navArgument("teamA") { type = NavType.StringType },
                navArgument("teamB") { type = NavType.StringType }
            )
        ) {
            backStackEntry ->
            val teamA = backStackEntry.arguments?.getString("teamA")
                ?.let { Uri.decode(it) }
                ?: return@composable
            val teamB = backStackEntry.arguments?.getString("teamB")
                ?.let { Uri.decode(it) }
                ?: return@composable

            HeadToHeadScreen(
                teamA = teamA,
                teamB = teamB,
                onAction = { action ->
                    when(action){
                        is HeadToHeadAction.CloseScreen -> {
                            navController.popBackStack()
                        }
                        is HeadToHeadAction.NavigateToDetail -> {
                            navController.navigate(Screen.detailMatchRoute(action.matchId))
                        }
                    }
                }
            )
        }
    }
}
