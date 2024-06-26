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

package com.algorand.android.modules.transaction.common.data.model

import com.google.gson.annotations.SerializedName

data class KeyRegTransactionResponse(
    @SerializedName("vote-participation-key")
    val voteParticipationKey: String?,
    @SerializedName("selection-participation-key")
    val selectionParticipationKey: String?,
    @SerializedName("state-proof-key")
    val stateProofKey: String?,
    @SerializedName("vote-first-valid")
    val voteFirstValid: Long?,
    @SerializedName("vote-last-valid")
    val voteLastValid: Long?,
    @SerializedName("vote-key-dilution")
    val voteKeyDilution: Long?,
    @SerializedName("non-participation")
    val nonParticipation: Boolean?
)
