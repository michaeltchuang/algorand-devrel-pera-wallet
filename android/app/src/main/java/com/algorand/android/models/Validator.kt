package com.algorand.android.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "validators")
data class Validator(
    @PrimaryKey val id: Long = 0L,
    val name: String,
    val algoStaked: Long?,
    val apy: Double?,
    val avatar: String?,
    val percentToValidator: Long?,
    val epochRoundLength: Long?,
    val minEntryStake: Long?,
    val inactive: Boolean = false,
    val lastPayout: Long?,
)
