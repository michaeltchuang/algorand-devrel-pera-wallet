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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.algorand.android.R
import com.algorand.android.models.Validator
import toAlgoFromMicro
import truncatedAlgorandAddress
import java.math.RoundingMode

@SuppressLint("ComposableNaming")
@Composable
fun validatorDetailScreen(
    tag: String,
    navController: NavController,
    accountAddress: String,
    validatorId: Long = 0L,
    stakeAmount: Long = 0L
) {

    val viewModel = hiltViewModel<ValidatorDetailViewModel>()
    viewModel.accountAddress = accountAddress
    viewModel.stakeAmount = stakeAmount.toBigDecimal()
    viewModel.refreshValidatorData(validatorId)

    val accountAlgoAmountState = viewModel.accountAlgoAmountState.collectAsStateWithLifecycle()
    viewModel.fetchMaxAccountAlgo()

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .height(100.dp)
        ) {
            validatorDetailTopLayout(
                onStakeAmountDetermined = {
                    viewModel.stakeAmount =
                        accountAlgoAmountState.value.toBigDecimal()
                            .toAlgoFromMicro()
                            .setScale(6, RoundingMode.HALF_EVEN)
                    viewModel.stakeAlgoTextFieldStateFlow.value = viewModel.stakeAmount.toString()
                },
                algoAmount = accountAlgoAmountState.value,
                accountAddress = viewModel.accountAddress
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.height(300.dp)
        ) {
            validatorDetailTabs(
                navController,
                viewModel,
                validatorId
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(100.dp)
        ) {
            validatorInfoRow(
                viewModel = viewModel,
                validatorId = validatorId
            )
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun content(
    validator: Validator,
    viewModel: ValidatorDetailViewModel
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Text(
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            text = "Stake ${viewModel.stakeAmount} ALGO",
        )
        Text(
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            text = viewModel.getAlgoUSDPrice(viewModel.stakeAmount),
        )

        Spacer(modifier = Modifier.height(50.dp))

        val formattedAddress = truncatedAlgorandAddress(viewModel.accountAddress)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            validatorDetailRowItem(
                label = stringResource(id = R.string.validator_detail_label_account),
                value = formattedAddress,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            validatorDetailRowItem(
                label = stringResource(id = R.string.validator_detail_label_validator),
                value = validator.name,
                shape = RoundedCornerShape(0.dp))
            validatorDetailRowItem(
                stringResource(id = R.string.validator_detail_label_apy),
                value = "${validator.apy}%",
                shape = RoundedCornerShape(0.dp))
            validatorDetailRowItem(
                stringResource(id = R.string.validator_detail_label_fee),
                value = "${validator.percentToValidator?.div(TEN_K)}%",
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        }

        Spacer(modifier = Modifier.weight(1F))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier =
            Modifier
                .padding(50.dp)
                .height(300.dp)
                .fillMaxWidth(),
        ) {
            Button(
                onClick = {
                    // (LocalContext.current).showAlertDialog(title = "", message =
                          // stringResource(id = R.string.validator_detail_button_wait_message))
                },
            ) {
                Text(text = stringResource(id = R.string.validator_detail_confirm_stake))
            }
        }
    }
}
