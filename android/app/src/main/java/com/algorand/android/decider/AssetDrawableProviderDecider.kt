/*
 * Copyright 2022 Pera Wallet, LDA
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.algorand.android.decider

import com.algorand.android.models.AssetInformation.Companion.ALGO_ID
import com.algorand.android.utils.assetdrawable.AlgoDrawableProvider
import com.algorand.android.utils.assetdrawable.AssetDrawableProvider
import com.algorand.android.utils.assetdrawable.BaseAssetDrawableProvider
import javax.inject.Inject

class AssetDrawableProviderDecider @Inject constructor() {

    fun getAssetDrawableProvider(assetId: Long): BaseAssetDrawableProvider {
        return when (assetId) {
            ALGO_ID -> AlgoDrawableProvider()
            else -> AssetDrawableProvider()
        }
    }
}