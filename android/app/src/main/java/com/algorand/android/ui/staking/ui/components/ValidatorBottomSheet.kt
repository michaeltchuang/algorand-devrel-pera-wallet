package com.algorand.android.ui.staking.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorand.android.R
import com.algorand.android.models.Validator
import kotlinx.coroutines.launch

enum class ValidatorBottomSheet {
    HIDE,
    VALIDATOR,
    VALIDATOR_DETAILS,
    APY,
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun validatorBottomSheet(
    showBottomSheet: ValidatorBottomSheet,
    onUpdateBottomSheetState: (showBottomSheetState: ValidatorBottomSheet) -> Unit,
    validator: Validator? = null
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    if (showBottomSheet != ValidatorBottomSheet.HIDE) {
        ModalBottomSheet(
            onDismissRequest = {
                onUpdateBottomSheetState(ValidatorBottomSheet.HIDE)
            },
            sheetState = sheetState,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier =
                Modifier
                    .padding(15.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(id = R.string.validator_label),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    text =
                    if (showBottomSheet == ValidatorBottomSheet.VALIDATOR) {
                        stringResource(id = R.string.validator_label)
                    } else if (showBottomSheet == ValidatorBottomSheet.APY) {
                        stringResource(id = R.string.validator_est_apy_long)
                    } else if (showBottomSheet == ValidatorBottomSheet.VALIDATOR_DETAILS) {
                        stringResource(id = R.string.validator_reti_label) + " ${validator?.id} Details"
                    } else {
                        ""
                    },
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier =
                Modifier
                    .padding(15.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    text =
                    if (showBottomSheet == ValidatorBottomSheet.VALIDATOR) {
                        stringResource(id = R.string.validator_label_desc)
                    } else if (showBottomSheet == ValidatorBottomSheet.APY) {
                        stringResource(id = R.string.validator_est_apy_desc)
                    } else if (showBottomSheet == ValidatorBottomSheet.VALIDATOR_DETAILS) {
                        "Useful Info Goes Here"
                    } else {
                        ""
                    },
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier =
                Modifier
                    .padding(15.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier.padding(bottom = 25.dp),
                    onClick = {
                        scope.launch {
                            sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onUpdateBottomSheetState(ValidatorBottomSheet.HIDE)
                            }
                        }
                    },
                ) {
                    Text("Done")
                }
            }
        }
    }
}
