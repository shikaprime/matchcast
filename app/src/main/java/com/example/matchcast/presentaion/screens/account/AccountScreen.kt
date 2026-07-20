package com.example.matchcast.presentaion.screens.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchcast.presentaion.screens.account.states.AccountAction
import com.example.matchcast.presentaion.screens.account.states.AccountEvent
import com.example.matchcast.presentaion.screens.account.states.AccountState
import com.example.matchcast.presentaion.theme.components.AccountContent
import com.example.matchcast.presentaion.theme.components.FullScreenError
import com.example.matchcast.presentaion.theme.components.FullScreenLoading

@Composable
fun AccountScreen(
    onAction: (AccountAction) -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect { action ->
            onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.obtainEvent(AccountEvent.EnterScreen)
    }

    when (val state = viewState) {
        is AccountState.Loading -> FullScreenLoading()
        is AccountState.Error -> FullScreenError(
            iconRes = state.icon,
            message = state.description,
            onRetry = {
                viewModel.obtainEvent(AccountEvent.EnterScreen)
            }
        )
        is AccountState.Display -> AccountContent(
            name = state.name,
            email = state.email,
            photo = state.photo,
            onBackClick = {
                viewModel.obtainEvent(AccountEvent.OnBackClick)
            },
            onResetPasswordClick = {
                viewModel.obtainEvent(AccountEvent.OnResetPasswordClick)
            },
            onSignOutClick = {
                viewModel.obtainEvent(AccountEvent.OnSignOutClick)
            }
        )
    }
}
