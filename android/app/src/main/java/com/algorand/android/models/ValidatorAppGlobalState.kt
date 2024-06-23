package com.algorand.android.models

import java.math.BigInteger

data class ValidatorAppGlobalState(
    // Only keys we care about on Android
    var algodVersion: String = "",
    var ewma: BigInteger = BigInteger.ZERO,
    var lastPayout: BigInteger = BigInteger.ZERO,
    var poolId: BigInteger = BigInteger.ZERO,
)
