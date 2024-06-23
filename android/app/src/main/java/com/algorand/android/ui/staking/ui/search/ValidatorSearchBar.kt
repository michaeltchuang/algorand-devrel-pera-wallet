package com.algorand.android.ui.staking.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.algorand.android.ui.staking.ui.search.ValidatorSearchViewModel

@SuppressLint("ComposableNaming")
@Composable
fun validatorSearchBar(validatorsListViewModel: ValidatorSearchViewModel) {
    val validatorSearchQuery = validatorsListViewModel.searchQuery.collectAsStateWithLifecycle()
    TextField(
        singleLine = true,
        value = validatorSearchQuery.value,
        modifier =
        Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        onValueChange = { searchQuery ->
            validatorsListViewModel.onValidatorSearchQueryChange(searchQuery)
        },
        trailingIcon = {
            if (validatorSearchQuery.value.isNotEmpty()) {
                Icon(
                    modifier =
                    Modifier.clickable {
                        validatorsListViewModel.onValidatorSearchQueryChange("")
                    },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                )
            }
        },
    )
}
