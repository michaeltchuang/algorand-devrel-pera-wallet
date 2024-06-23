package com.algorand.android.ui.staking.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.algorand.android.R
import com.algorand.android.ui.staking.ui.detail.validatorDetailScreen
import com.algorand.android.ui.staking.ui.search.validatorSearchScreen
import com.algorand.android.ui.staking.ui.staked.validatorStakedPoolsScreen

@SuppressLint("ComposableNaming")
@Composable
fun stakingNavigation(accountAddress: String, onNavigateBackToFragment: () -> Unit) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val appBarTitle = remember { mutableStateOf("Reti Validators") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(100.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_left_arrow),
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 25.dp, top = 10.dp, bottom = 10.dp)
                            .clickable {
                                if (navController.currentDestination?.route == Screen.ValidatorSearchScreen.route) {
                                    onNavigateBackToFragment.invoke()
                                } else {
                                    navController.navigateUp()
                                }
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(end = 100.dp)
                        .wrapContentHeight()
                        .weight(1F),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = appBarTitle.value,
                        // fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier)
        },
        bottomBar = { },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.ValidatorSearchScreen.route,
            route = "parentRoute",
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {
            composable(Screen.ValidatorSearchScreen.route) {
                navBackStackEntry ->
                appBarTitle.value = "Reti Validators"
                validatorSearchScreen(
                    tag = Screen.ValidatorSearchScreen.route,
                    navController = navController,
                    accountAddress = accountAddress
                )
                snackBarLayout(snackbarHostState = snackbarHostState)
            }
            composable(Screen.ValidatorStakedScreen.route
            ) { navBackStackEntry ->
                appBarTitle.value = "Staked Reti Validators"
                validatorStakedPoolsScreen(
                    tag = Screen.ValidatorDetailScreen.route,
                    navController = navController,
                    accountAddress = accountAddress
                )
                snackBarLayout(snackbarHostState = snackbarHostState)
            }
            composable(Screen.ValidatorDetailScreen.route + "/{validatorId}",
                arguments = listOf(
                    navArgument("validatorId") { type = NavType.LongType }
                )
            ) { navBackStackEntry ->
                val validatorId = (navBackStackEntry.arguments as Bundle).get("validatorId") as Long
                appBarTitle.value = "Reti Validator $validatorId"
                validatorDetailScreen(
                    tag = Screen.ValidatorDetailScreen.route,
                    navController = navController,
                    accountAddress = accountAddress,
                    validatorId = validatorId
                )
                snackBarLayout(snackbarHostState = snackbarHostState)
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun snackBarLayout(
    snackbarHostState: SnackbarHostState,
) {
    // We're using existing app xml alerts for now
    val scope = rememberCoroutineScope()
//    val snackBarStateFlow by algorandBaseViewModel.snackBarStateFlow.collectAsStateWithLifecycle()
//    if (snackBarStateFlow.trim().length > 0) {
//        // val context = LocalContext.current
//        LaunchedEffect(Unit) {
//            scope.launch {
//                snackbarHostState.showSnackbar(
//                    duration = SnackbarDuration.Short,
//                    message = snackBarStateFlow,
//                )
//                // Toast.makeText(context, snackBarStateFlow, Toast.LENGTH_SHORT).show()
//                launch {
//                    delay(1000L)
//                    // algorandBaseViewModel.setSnackBarMessage("")
//                }
//            }
//        }
//    }
}

sealed class Screen(
    val route: String,
) {
    object ValidatorDetailScreen : Screen("validator/detail")

    object ValidatorSearchScreen : Screen("validator/search")

    object ValidatorStakedScreen : Screen("validator/staked")
}
