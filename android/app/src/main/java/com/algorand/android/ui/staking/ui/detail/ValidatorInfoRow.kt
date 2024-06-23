package com.algorand.android.ui.staking.ui.detail

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import calculateRewardBlock
import com.algorand.android.R

@SuppressLint("ComposableNaming")
@Composable
fun validatorInfoRow(viewModel: ValidatorDetailViewModel, validatorId: Long) {
    val validatorDetailUIState = viewModel.validatorDetailUIState.collectAsStateWithLifecycle()
    val currentRoundState = viewModel.currentRoundState.collectAsStateWithLifecycle()
    val mainHandler = Handler(Looper.getMainLooper())
    var refresh = Runnable {}
    refresh =
        Runnable {
            viewModel.getCurrentRound()
            mainHandler.postDelayed(refresh, 2000)
        }
    mainHandler.postDelayed(refresh, 2000)
    viewModel.fetchValidatorById(validatorId)
    when (val uiState = validatorDetailUIState.value) {
        is ValidatorDetailUIState.Error -> {
            Text("Error: ${uiState.message}")
        }

        is ValidatorDetailUIState.Loading -> {
            // show placeholder UI
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(id = R.string.loading),
                )
            }
        }

        is ValidatorDetailUIState.Success -> {
            LazyColumn {
                item {
                    validatorDetailBottomLayout(
                        validator = uiState.result,
                        currentBlock = currentRoundState.value,
                        rewardBlock = calculateRewardBlock(
                            currentRound = currentRoundState.value,
                            lastPayout = uiState.result.lastPayout ?: 0L,
                            epochRoundLength = uiState.result.epochRoundLength ?: 0L
                        )
                    )
                }
            }
        }
    }
}
