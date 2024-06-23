package com.algorand.android.network

import com.algorand.android.models.NfdRecord
import com.algorand.android.models.Valid
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NfdApi {
    @GET("/nfd/isValid/{appId}")
    suspend fun isValid(@Path("appId") appId: Int): Response<Valid>

    @GET("/nfd/{nfd}")
    suspend fun fetchNfd(@Path("nfd") nfd: String): Response<NfdRecord>
}
