package com.example.matchcast.presentaion.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


@Serializable
object HomeRoute

@Serializable
data class MatchDetailsRoute(val matchId: String)



// =====================================================================
// 2. ГЛАВНЫЙ КЛАСС / ФУНКЦИЯ ГРАФА НАВИГАЦИИ
// =====================================================================

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute, // Какой экран откроется первым
        modifier = modifier
    ) {

        // --- ТЕХНИЧЕСКОЕ ОПИСАНИЕ ЭКРАНА "ГЛАВНЫЙ" ---
        composable<HomeRoute> {
            /* Здесь мы вызываем твою реальную Composable-функцию HomeScreen.
               Вместо того чтобы передавать внутрь HomeScreen сам navController,
               мы перехватываем его внутренние события (UiEvent / Action) прямо здесь.
            */
            HomeScreen(
                onEvent = { event ->
                    when (event) {
                        // Если на главном экране кликнули по матчу — навигируем на детали
                        is HomeUiEvent.OnMatchClick -> {
                            navController.navigate(MatchDetailsRoute(matchId = event.id))
                        }

                        else -> {}
                    }
                }
            )
        }

        // --- ТЕХНИЧЕСКОЕ ОПИСАНИЕ ЭКРАНА "ДЕТАЛИ МАТЧА" ---
        composable<MatchDetailsRoute> { backStackEntry ->
            /*
               Jetpack Navigation автоматически извлекает переданные параметры.
               С помощью функции toRoute() мы получаем готовый объект MatchDetailsRoute
               и можем безопасно достать из него matchId без использования ручных Bundle и String-ключей.
            */
            val route: MatchDetailsRoute = backStackEntry.toRoute()

            MatchDetailsScreen(
                matchId = route.matchId,
                onEvent = { event ->
                    when (event) {
                        // Если на экране деталей нажали кнопку "Назад"
                        is MatchDetailsUiEvent.OnBackClick -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }


    }
}


