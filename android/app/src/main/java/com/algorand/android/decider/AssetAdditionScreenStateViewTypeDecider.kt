/*
 * Copyright 2022 Pera Wallet, LDA
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License
 *
 */

package com.algorand.android.decider

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.algorand.android.R
import com.algorand.android.models.ScreenState
import com.algorand.android.modules.assets.addition.ui.model.AssetAdditionType
import java.io.IOException
import javax.inject.Inject

class AssetAdditionScreenStateViewTypeDecider @Inject constructor() {

    fun decideScreenStateViewType(
        combinedLoadStates: CombinedLoadStates,
        itemCount: Int,
        assetAdditionType: AssetAdditionType
    ): ScreenState? {
        val isCurrentStateError = combinedLoadStates.refresh is LoadState.Error
        val isLoading = combinedLoadStates.refresh is LoadState.Loading
        val isEmpty = isLoading.not() && itemCount == 0 && isCurrentStateError.not()

        return when {
            isCurrentStateError -> {
                val throwable = (combinedLoadStates.refresh as LoadState.Error).error
                if (throwable is IOException) {
                    ScreenState.ConnectionError()
                } else {
                    // TODO: 7.01.2022 Specify error state action and message
                    ScreenState.DefaultError()
                }
            }
            isEmpty -> ScreenState.CustomState(title = decideEmptyStateTitle(assetAdditionType))
            else -> null
        }
    }

    private fun decideEmptyStateTitle(assetAdditionType: AssetAdditionType): Int {
        return when (assetAdditionType) {
            AssetAdditionType.ASSET -> R.string.no_asset_found
            AssetAdditionType.COLLECTIBLE -> R.string.no_nft_found
        }
    }
}
