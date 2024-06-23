package com.algorand.android.ui.staking.ui.search

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.algorand.android.ui.staking.navigation.Screen

@SuppressLint("ComposableNaming")
@Composable
fun validatorSearchScreen(
    tag: String,
    navController: NavController,
    accountAddress: String
) {
    val viewModel = hiltViewModel<ValidatorSearchViewModel>()
    viewModel.accountAddress = accountAddress

    validatorSearchWidget(
        onValidatorSelected = { validatorId ->
            navController.navigate(Screen.ValidatorDetailScreen.route + "/$validatorId")
        },
    )
}
