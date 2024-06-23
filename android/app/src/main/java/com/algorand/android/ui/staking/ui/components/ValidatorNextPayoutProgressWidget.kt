package com.algorand.android.ui.staking.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algorand.android.R
import com.algorand.android.models.Validator

@SuppressLint("ComposableNaming")
@Composable
fun validatorNextPayoutWidget(
    validator: Validator,
    currentRound: Long,
    rewardBlock: Long
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            text = "Current Block: "
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            text = if (currentRound != 0L) "#$currentRound" else "--------"
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            text = "  |  "
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            text = "Next Reward: "
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            text = if (rewardBlock != 0L) "#$rewardBlock" else "--------"
        )
    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(5.dp)
    ) {
        if (currentRound != 0L && rewardBlock != 0L) {
            LinearProgressIndicator(
                color = colorResource(id = R.color.yellow_500),
                trackColor = MaterialTheme.colorScheme.primary,
                progress = { calculateRewardProgress(
                    currentRound = currentRound,
                    rewardBlock = rewardBlock,
                    epochRoundLength = validator.epochRoundLength ?: 0L
                ) },
                modifier = Modifier
                    .height(3.dp)
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview
@SuppressLint("ComposableNaming")
@Composable
fun validatorNextPayoutProgressWidgetEample() {
    validatorNextPayoutWidget(
        validator = Validator(
            id = 3L,
            name = "algoleaguestestnet.algo",
            algoStaked = 25000L,
            apy = 3.03,
            avatar = "https://app.nf.domains/img/nfd-image-placeholder.jpg",
            percentToValidator = 5,
            epochRoundLength = 10000,
            minEntryStake = 10,
            lastPayout = 41367026L
        ),
        currentRound = 40160735,
        rewardBlock = 40160735
    )
}

fun calculateRewardProgress(
    currentRound: Long,
    rewardBlock: Long,
    epochRoundLength: Long
): Float {
    return if (rewardBlock != 0L && epochRoundLength != 0L &&
        currentRound != 0L) {
        (rewardBlock - currentRound).toDouble().div(epochRoundLength).toFloat()
    } else {
        0F
    }
}
