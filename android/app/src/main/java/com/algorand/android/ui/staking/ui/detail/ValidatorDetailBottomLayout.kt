package com.algorand.android.ui.staking.ui.detail

import TEN_K
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algorand.android.R
import com.algorand.android.models.Validator
import com.algorand.android.ui.staking.ui.components.ValidatorBottomSheet
import com.algorand.android.ui.staking.ui.components.validatorBottomSheet
import com.algorand.android.ui.staking.ui.components.validatorNextPayoutWidget
import com.algorand.android.ui.staking.ui.components.validatorSearchItemIconColumn
import truncatedAlgorandAddress

@SuppressLint("ComposableNaming")
@Composable
fun validatorDetailBottomLayout(
    validator: Validator,
    currentBlock: Long,
    rewardBlock: Long
) {
    var showBottomSheet by remember { mutableStateOf(ValidatorBottomSheet.HIDE) }
    validatorNextPayoutWidget(
        validator = validator,
        currentRound = currentBlock,
        rewardBlock = rewardBlock
    )
    Row(
        modifier =
        Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        validatorSearchItemIconColumn(validator)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier =
            Modifier
                .padding(end = 15.dp)
                .fillMaxHeight()
                .weight(1f),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = truncatedAlgorandAddress(validator.name),
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    text = " (Validator ${validator.id})"
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    text = "${validator.apy}% APY"
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                    text = "*"
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    text = if (validator.percentToValidator == null) {
                        "Unknown Fee"
                    } else if (validator.percentToValidator == 0L) {
                        "0% Fee"
                    } else {
                        "${validator.percentToValidator.div(TEN_K)}% Fee"
                    }
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier =
            Modifier
                .fillMaxHeight()
                .width(50.dp),
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = stringResource(id = R.string.validator_label),
                // tint = MaterialTheme.colorScheme.primary,
                modifier =
                Modifier.clickable {
                    showBottomSheet = ValidatorBottomSheet.VALIDATOR_DETAILS
                },
            )
        }
    }
    validatorBottomSheet(
        showBottomSheet = showBottomSheet,
        onUpdateBottomSheetState = { showBottomSheetState ->
            showBottomSheet = showBottomSheetState
        },
        validator = validator
    )
}

@Preview
@SuppressLint("ComposableNaming")
@Composable
fun validatorDetailBottomLayoutExample() {
    validatorDetailBottomLayout(
        validator = Validator(
            id = 14,
            name = "algoleaguestestnet.algo",
            algoStaked = 25000,
            apy = 3.03,
            avatar = "https://app.nf.domains/img/nfd-image-placeholder.jpg",
            percentToValidator = 5,
            epochRoundLength = 10000,
            minEntryStake = 10,
            lastPayout = 41367026L
        ), 40161936, 40161936
    )
}
