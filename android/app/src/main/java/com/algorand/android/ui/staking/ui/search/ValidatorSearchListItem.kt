package com.algorand.android.ui.staking.ui.search

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorand.android.models.Validator
import com.algorand.android.ui.staking.ui.components.validatorSearchItemIconColumn
import toAlgoFromMicro
import truncatedAlgorandAddress

@SuppressLint("ComposableNaming")
@Composable
fun validatorSearchListItem(
    validator: Validator,
    onValidatorSelected: (validatorId: Long) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .clickable {
                    onValidatorSelected(validator.id)
                }
                .height(100.dp)
                .fillMaxWidth()
                .padding(5.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        validatorSearchItemIconColumn(validator)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier =
                Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxHeight()
                    .weight(1f),
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                text = truncatedAlgorandAddress(validator.name),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                text =
                    when (validator.algoStaked) {
                        null -> "---"
                        else -> "${validator.algoStaked.toAlgoFromMicro()} ALGO Staked"
                    },
            )
        }
        validatorSearchItemApyColumn(validator)
    }
}
