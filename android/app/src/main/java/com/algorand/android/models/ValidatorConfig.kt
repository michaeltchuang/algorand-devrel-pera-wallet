package com.algorand.android.models

import com.algorand.algosdk.crypto.Address
import com.algorand.android.ui.staking.helpers.Constants
import java.math.BigInteger

data class ValidatorConfig(
    var ID: BigInteger = BigInteger.ZERO,
    var Owner: Address = Address.forApplication(Constants.RETI_APP_ID_TESTNET),
    var Manager: Address = Address.forApplication(Constants.RETI_APP_ID_TESTNET),
    var NFDForInfo: BigInteger = BigInteger.ZERO,
    var EntryGatingType: BigInteger = BigInteger.ZERO,
    var EntryGatingAddress: Address = Address.forApplication(Constants.RETI_APP_ID_TESTNET),
    var EntryGatingAssets: Array<*> = arrayOf<Any>(),
    var GatingAssetMinBalance: BigInteger = BigInteger.ZERO,
    var RewardTokenId: BigInteger = BigInteger.ZERO,
    var RewardPerPayout: BigInteger = BigInteger.ZERO,
    var EpochRoundLength: BigInteger = BigInteger.ZERO,
    var PercentToValidator: BigInteger = BigInteger.ZERO,
    var ValidatorCommissionAddress: Address = Address.forApplication(Constants.RETI_APP_ID_TESTNET),
    var MinEntryStake: BigInteger = BigInteger.ZERO,
    var MaxAlgoPerPool: BigInteger = BigInteger.ZERO,
    var PoolsPerNode: BigInteger = BigInteger.ZERO,
    var SunsettingOn: BigInteger = BigInteger.ZERO,
    var SunsettingTo: BigInteger = BigInteger.ZERO
)
