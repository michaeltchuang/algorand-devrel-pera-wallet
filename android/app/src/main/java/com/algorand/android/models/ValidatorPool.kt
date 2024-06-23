package com.algorand.android.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "validatorPools")
data class ValidatorPool(
    @PrimaryKey var poolAppId: Long = 0L,
    var validatorId: Long = 0L,
    var totalStakers: Long = 0L,
    var totalAlgoStaked: Long = 0L,

    // additional global state pool
    var algodVersion: String = "",
    var lastPayout: Long = 0L,
    var ewma: Long = 0L,
    var poolId: Long = 0L,
)
