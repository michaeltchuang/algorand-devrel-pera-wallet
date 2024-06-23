package com.algorand.android.models

import kotlinx.serialization.Serializable

@Serializable
data class NfdRecord(
    val appID: Long? = null,
    val name: String,
    val properties: NfdProperties? = null,
)

@Serializable
data class NfdProperties(
    // Define properties of NFDProperties if needed
    val userDefined: NfdUserDefined? = null
)

@Serializable
data class NfdUserDefined(
    // Define properties of NFDProperties if needed
    val avatar: String? = null
)
