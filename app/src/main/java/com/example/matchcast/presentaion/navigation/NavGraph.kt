package com.example.matchcast.presentaion.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

import com.example.matchcast.presentaion.screens.listmatch.ListMatchAction
import com.example.matchcast.presentaion.screens.detail.DetailMatchAction

@Serializable
object ListMatchRoute

@Serializable
data class DetailMatchRoute(val matchId: Int)


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ListMatchRoute,
        modifier = modifier
    ) {

        composable<ListMatchRoute> {
            ListMatchScreen(
                onAction = { action ->
                    when (action) {
                        is ListMatchAction.NavigateToDetail -> {
                            navController.navigate(DetailMatchRoute(matchId = action.matchId))
                        }
                        is ListMatchAction.CloseScreen -> {
                        }
                    }
                }
            )
        }

        composable<DetailMatchRoute> { backStackEntry ->
            val route: DetailMatchRoute = backStackEntry.toRoute()

            DetailMatchScreen(
                matchId = route.matchId,
                onAction = { action ->
                    when (action) {
                        is DetailMatchAction.CloseScreen -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ListMatchScreen(onAction: (ListMatchAction) -> Unit) {}

@Composable
fun DetailMatchScreen(matchId: Int, onAction: (DetailMatchAction) -> Unit) {}


