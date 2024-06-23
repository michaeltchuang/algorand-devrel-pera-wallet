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

package com.algorand.android.dependencyinjection

import com.algorand.android.BuildConfig
import com.algorand.android.models.Account
import com.algorand.android.models.AccountDeserializer
import com.algorand.android.network.AlgodApi
import com.algorand.android.network.AlgodInterceptor
import com.algorand.android.network.IndexerApi
import com.algorand.android.network.IndexerInterceptor
import com.algorand.android.network.MobileAlgorandApi
import com.algorand.android.network.MobileHeaderInterceptor
import com.algorand.android.network.NfdApi
import com.algorand.android.ui.staking.helpers.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hipo.hipoexceptionsandroid.RetrofitErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Module which provides all required dependencies about network
 */
// Safe here as we are dealing with a Dagger 2 module
@Suppress("TooManyFunctions")
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_CONSTANT = 60L

    @Provides
    @Singleton
    fun provideNodeInterceptor(): IndexerInterceptor {
        return IndexerInterceptor()
    }

    @Provides
    @Singleton
    fun provideAlgodInterceptor(): AlgodInterceptor {
        return AlgodInterceptor()
    }

    @Provides
    @Singleton
    fun provideNodeHeaderInterceptor(): MobileHeaderInterceptor {
        return MobileHeaderInterceptor()
    }

    @Provides
    @Singleton
    @Named("indexerHttpClient")
    fun provideHttpClient(
        indexerInterceptor: IndexerInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(indexerInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("algodHttpClient")
    fun provideAlgodHttpClient(
        algodInterceptor: AlgodInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(algodInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("nfdHttpClient")
    fun provideNfdHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("mobileAlgorandHttpClient")
    fun provideMobileAlgorandHttpClient(
        mobileHeaderInterceptor: MobileHeaderInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mobileHeaderInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("algodExplorerPriceHttpClient")
    fun provideAlgodExplorerPriceHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("walletConnectHttpClient")
    fun provideWalletConnectHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_CONSTANT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().registerTypeAdapter(Account::class.java, AccountDeserializer()).create()
    }

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    internal fun provideHipoExceptionHandler(gson: Gson): RetrofitErrorHandler {
        return RetrofitErrorHandler(gson)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Singleton
    @Named("algodRetrofitInterface")
    internal fun provideAlgodRetrofitInterface(
        @Named("algodHttpClient") algodHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ALGORAND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(algodHttpClient)
            .build()
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Singleton
    @Named("nfdRetrofitInterface")
    internal fun provideNfdRetrofitInterface(
        @Named("nfdHttpClient") nfdHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.RETI_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(nfdHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("indexerRetrofitInterface")
    internal fun provideIndexerRetrofitInterface(
        @Named("indexerHttpClient") indexerHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ALGORAND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(indexerHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("mobileAlgorandRetrofitInterface")
    internal fun provideMobileAlgorandRetrofitInterface(
        @Named("mobileAlgorandHttpClient") mobileAlgorandHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MOBILE_ALGORAND_MAINNET_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(mobileAlgorandHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideAlgodApi(
        @Named("algodRetrofitInterface") algodRetrofitInterface: Retrofit
    ): AlgodApi {
        return algodRetrofitInterface.create(AlgodApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideNfdApi(
        @Named("nfdRetrofitInterface") nfdRetrofitInterface: Retrofit
    ): NfdApi {
        return nfdRetrofitInterface.create(NfdApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideIndexerApi(
        @Named("indexerRetrofitInterface") indexerRetrofitInterface: Retrofit
    ): IndexerApi {
        return indexerRetrofitInterface.create(IndexerApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideMobileAlgorandApi(
        @Named("mobileAlgorandRetrofitInterface") mobileAlgorandRetrofitInterface: Retrofit
    ): MobileAlgorandApi {
        return mobileAlgorandRetrofitInterface.create(MobileAlgorandApi::class.java)
    }
}
