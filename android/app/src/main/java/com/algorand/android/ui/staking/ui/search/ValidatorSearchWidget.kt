package com.algorand.android.ui.staking.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.algorand.android.R
import com.algorand.android.ui.staking.ui.components.ValidatorBottomSheet
import com.algorand.android.ui.staking.ui.components.validatorBottomSheet
import com.algorand.android.ui.staking.ui.validatorSearchBar

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun validatorSearchWidget(onValidatorSelected: (validatorId: Long) -> Unit) {
    val viewModel = hiltViewModel<ValidatorSearchViewModel>()
    val validatorListUIState = viewModel.validatorsListUIState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(ValidatorBottomSheet.HIDE) }
    var showNoResults by remember { mutableStateOf(false) }

    Column(
        modifier =
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        validatorSearchBar(viewModel)

        when (val uiState = validatorListUIState.value) {
            is ValidatorsListUIState.Error -> {
                Text("Error: ${uiState.message}")
            }

            is ValidatorsListUIState.Loading -> {
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

            is ValidatorsListUIState.Success -> {
                LazyColumn {
                    stickyHeader {
                        Row(
                            modifier =
                                Modifier
                                    .background(color = Color.White)
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier =
                                    Modifier
                                        .padding(start = 10.dp)
                                        .fillMaxHeight()
                                        .weight(1f),
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
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        text = stringResource(id = R.string.validator_label),
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = stringResource(id = R.string.validator_label),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier =
                                            Modifier.clickable {
                                                showBottomSheet = ValidatorBottomSheet.VALIDATOR
                                            },
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End,
                                modifier =
                                    Modifier
                                        .padding(end = 10.dp)
                                        .fillMaxHeight()
                                        .weight(1f),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier =
                                        Modifier
                                            .padding(15.dp)
                                            .wrapContentHeight()
                                            .fillMaxWidth(),
                                ) {
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        text = stringResource(id = R.string.validator_est_apy),
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = stringResource(id = R.string.validator_est_apy),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.clickable { showBottomSheet = ValidatorBottomSheet.APY },
                                    )
                                }
                            }
                        }
                    }

                    showNoResults = uiState.result.isEmpty()
                    items(
                        items = uiState.result,
                        itemContent = { validator ->
                            validatorSearchListItem(validator, onValidatorSelected)
                        },
                    )
                }

                if (showNoResults) {
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
                            text = stringResource(id = R.string.validator_no_results),
                        )
                    }
                    viewModel.setupDB()
                }
            }
        }

        validatorBottomSheet(showBottomSheet, onUpdateBottomSheetState = { showBottomSheetState ->
            showBottomSheet = showBottomSheetState
        })
    }
}
