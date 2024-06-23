package com.algorand.android.ui.staking.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorand.android.models.Validator

@SuppressLint("ComposableNaming")
@Composable
fun validatorSearchItemApyColumn(validator: Validator) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
        modifier =
        Modifier
            .fillMaxHeight()
            .width(100.dp),
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier =
            Modifier
                .padding(end = 25.dp),
            text =
            when (validator.apy) {
                null -> "---"
                else -> "${validator.apy}%"
            },
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            text = "",
        )
    }
}
