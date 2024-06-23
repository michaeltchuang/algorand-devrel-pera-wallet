package com.algorand.android.ui.staking.ui.detail

import TEN_K
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import toAlgoFromMicro
import java.math.BigDecimal

@SuppressLint("ComposableNaming")
@Composable
fun stakingPoolTabContent(
    tabIndex: Int,
    viewModel: ValidatorDetailViewModel,
    validatorId: Long
) {
    val accountAlgoAmountState = viewModel.accountAlgoAmountStateFlow.collectAsState()
    val stakeAlgoTextFieldState = viewModel.stakeAlgoTextFieldState.collectAsState()
    viewModel.stakeAmount?.let {
        if (it == BigDecimal.ZERO) {
            viewModel.stakeAlgoTextFieldStateFlow.value = ""
        } else {
            viewModel.stakeAlgoTextFieldStateFlow.value = it.toString()
        }
    }
    val validStakingAmount: Boolean = if (viewModel.stakeAmount > BigDecimal.ZERO &&
        viewModel.stakeAmount <= accountAlgoAmountState.value.toBigDecimal().toAlgoFromMicro()) {
        true
    } else {
        false
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(200.dp)
        ) {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                text = "Stake")

            Spacer(Modifier.height(10.dp))

            TextField(
                value = stakeAlgoTextFieldState.value,
                textStyle = MaterialTheme.typography.headlineMedium,
                onValueChange = {
                    val floatInput = it.toFloatOrNull()
                    if (floatInput != null && it.length <= 15) {
                        viewModel.stakeAlgoTextFieldStateFlow.value = it
                        viewModel.stakeAmount = it.toBigDecimal()
                    } else if (it.length == 0) {
                        viewModel.stakeAlgoTextFieldStateFlow.value = ""
                        viewModel.stakeAmount = BigDecimal.ZERO
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )

            Spacer(Modifier.height(10.dp))

            Text(
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                text = "ALGO")

            Spacer(Modifier.height(10.dp))

            Text(
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                text = if (viewModel.stakeAmount == BigDecimal.ZERO) {
                    ""
                } else {
                    viewModel.getAlgoUSDPrice(viewModel.stakeAmount)
                       },
            )

            Spacer(Modifier.height(10.dp))

            Button(
                enabled = validStakingAmount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, top = 15.dp, bottom = 15.dp, end = 25.dp),
                onClick = {
                    val textfieldValue = viewModel.stakeAlgoTextFieldState.value.toDoubleOrNull()
                    val stakeAmount = if (textfieldValue == null) {
                        0
                    } else {
                        textfieldValue.times(TEN_K.toDouble())
                    }
                    viewModel.addStakeToValidator(
                        validatorId = validatorId,
                        poolAppId = viewModel.validatorPoolsState.value.get(tabIndex - 1).poolAppId,
                        stakeAmount = stakeAmount.toInt()
                    )
                },
            ) {
                Text("Confirm")
            }
        }
    }
}
