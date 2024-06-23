package com.algorand.android.ui.staking.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.algorand.android.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ComposableNaming")
@Composable
fun validatorDetailTabs(
    navController: NavController,
    viewModel: ValidatorDetailViewModel,
    validatorId: Long
) {
    viewModel.fetchValidatorPools(validatorId)
    val validatorPoolsState = viewModel.validatorPoolsState.collectAsStateWithLifecycle()
    val tabData = getTabList(validatorPoolsState.value.size)
    val pagerState = rememberPagerState(pageCount = { tabData.size })
    Column(
        modifier = Modifier
            .padding(top = 140.dp, bottom = 125.dp)
            .fillMaxSize()) {
        tabLayout(tabData, pagerState)
        tabContent(tabData, pagerState, validatorId, viewModel)
    }
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun tabLayout(
    tabData: List<Pair<String, ImageVector>>,
    pagerState: PagerState,
) {
    if (tabData.size < 4) {
        TabRow(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            selectedTabIndex = pagerState.currentPage,
            divider = {
                Spacer(modifier = Modifier.height(5.dp))
            },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 5.dp,
                    color = colorResource(id = R.color.yellow_500),
                )
            },
            modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            tabLayoutContent(tabData, pagerState)
        }
    } else {
        ScrollableTabRow(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            selectedTabIndex = pagerState.currentPage,
            divider = {
                Spacer(modifier = Modifier.height(5.dp))
            },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 5.dp,
                    color = colorResource(id = R.color.yellow_500),
                )
            },
            edgePadding = 0.dp,
            modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            tabLayoutContent(tabData, pagerState)
        }
    }
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun tabLayoutContent(tabData: List<Pair<String, ImageVector>>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    tabData.forEachIndexed { index, data ->
        LeadingIconTab(
            selected = pagerState.currentPage == index,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            icon = {
                Icon(imageVector = data.second, contentDescription = null)
            },
            text = {
                Text(text = data.first, fontSize = 12.sp)
            },
        )
    }
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun tabContent(
    tabData: List<Pair<String, ImageVector>>,
    pagerState: PagerState,
    validatorId: Long,
    viewModel: ValidatorDetailViewModel,
) {
    HorizontalPager(state = pagerState) { index ->
        when (index) {
            0 -> { poolRewardsTabContent(viewModel = viewModel) }
            in 1..tabData.size -> {
                stakingPoolTabContent(
                    tabIndex = index,
                    viewModel = viewModel,
                    validatorId = validatorId,
                )
            }
        }
    }
}

@Composable
private fun getTabList(pools: Int): List<Pair<String, ImageVector>> {
    val output = mutableListOf("Rewards" to ImageVector.vectorResource(R.drawable.ic_reward))
    for (index in 0 until pools) {
        output.add("Pool ${index + 1}" to ImageVector.vectorResource(R.drawable.ic_pool_24))
    }
    return output
}
