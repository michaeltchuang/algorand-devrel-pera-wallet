package com.algorand.android.ui.staking.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import toAlgoFromMicro
import truncatedAlgorandAddress
import java.math.BigInteger

@SuppressLint("ComposableNaming")
@Composable
fun validatorDetailTopLayout(
    onStakeAmountDetermined: () -> Unit,
    algoAmount: BigInteger,
    accountAddress: String
) {
    Column(
        modifier =
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
                    .padding(start = 25.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Balance:",
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    text = if (algoAmount.toInt() == 0) {
                        "0 ALGO"
                    } else {
                        "${algoAmount.toDouble().toAlgoFromMicro()} ALGO"
                    }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier,
                    onClick = {
                        onStakeAmountDetermined.invoke()
                    },
                ) {
                    Text("Max")
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 25.dp, bottom = 15.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Account Address:",
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    text = truncatedAlgorandAddress(accountAddress)
                )

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Preview
@SuppressLint("ComposableNaming")
@Composable
fun stakingAlgoMaxAmountRowItemExample() {
    validatorDetailTopLayout(
        onStakeAmountDetermined = { },
        algoAmount = "14990000".toBigInteger(),
        accountAddress = "FPH5SP6GXUA2CMLCMYVWWJJCTQXCNPKRR3QCAMBCQD3ILIV7FR27UIOL7M"
    )
}
