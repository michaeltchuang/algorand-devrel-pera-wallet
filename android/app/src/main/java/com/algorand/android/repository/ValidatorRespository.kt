package com.algorand.android.repository

import TEN_K
import TWO_HUNDRED_FORTY
import android.content.res.AssetManager
import android.util.Log
import com.algorand.algosdk.abi.Contract
import com.algorand.algosdk.abi.Method
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.MethodCallTransactionBuilder
import com.algorand.algosdk.builder.transaction.PaymentTransactionBuilder
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.AppBoxReference
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.transaction.TransactionWithSigner
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.algorand.android.database.ValidatorDao
import com.algorand.android.models.Valid
import com.algorand.android.models.Validator
import com.algorand.android.models.ValidatorAppGlobalState
import com.algorand.android.models.ValidatorConfig
import com.algorand.android.models.ValidatorPool
import com.algorand.android.network.NfdApi
import com.algorand.android.ui.staking.helpers.Constants.RETI_APP_ID_TESTNET
import getStakerLedgerBoxName
import getStakerPoolSetBoxName
import getValidatorListBoxName
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.math.BigInteger
import java.math.RoundingMode
import java.security.Security
import java.util.Base64

const val TAG = "ValidatorRepository"
const val ROUNDS_TO_WAIT = 250

class ValidatorRespository @Inject constructor(
    private val validatorDao: ValidatorDao,
    private val algodClient: AlgodClient?,
    private val nfdApi: NfdApi,
    private val assetManager: AssetManager
) {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val account: Account?
    var contract: Contract

    init {
        Security.removeProvider("BC")
        Security.insertProviderAt(BouncyCastleProvider(), 0)
        // useDummyAlgorandAccountToQuery
        account = recoverAccount()
        // loadValidatorDataIntoDb()

        val contractStr = assetManager.open("ValidatorRegistry.arc4.json")
            .bufferedReader()
            .use { it.readText() }
        contract = Encoder.decodeFromJson(contractStr, Contract::class.java)
    }

    fun recoverAccount(): Account? {
        val part1 = "panda course account pact six same"
        val part2 = "antique shed slender finger lab dose"
        val part3 = "reveal escape amateur since power left"
        val part4 = "trust update soup neck tuition about meadow"
        val passPhrase = "$part1 $part2 $part3 $part4"
        try {
            return Account(passPhrase)
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun insertValidatorIntoDb(validator: Validator) = validatorDao.insertValidator(validator)

    fun getAllValidatorsFromDb(): Flow<List<Validator>> = validatorDao.getAllValidatorsAsFlow()

    fun getValidatorByIdFromDb(validatorId: Long): Flow<Validator?> =
        validatorDao.getValidatorByIdAsFlow(validatorId)

    suspend fun insertValidatorPoolIntoDb(validatorPool: ValidatorPool) =
        validatorDao.insertValidatorPool(validatorPool)

    fun getAllValidatorPoolsFromDb(validatorId: Long): Flow<List<ValidatorPool>> =
        validatorDao.getAllValidatorPoolsAsFlow(validatorId)

    fun getValidatorPoolByIdFromDb(appPoolId: Long): Flow<ValidatorPool?> =
        validatorDao.getValidatorPoolByIdAsFlow(appPoolId)

    suspend fun getNumberOfValidators(): Long {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            val methodArgs = null
            val boxReferences = null
            val method = contract.getMethodByName("getNumValidators")

            account?.let {
                result = methodCallTransaction(
                    appId = RETI_APP_ID_TESTNET,
                    account = account,
                    method = method,
                    methodArgs = methodArgs,
                    boxReferences = boxReferences
                )
            }
        }
        val validatorCount =
            if (result?.confirmedRound == null) {
                0L
            } else if (result?.methodResults == null) {
                0L
            } else {
                // successful result
                result?.methodResults?.get(0)?.value as BigInteger
            }
        return validatorCount.toLong()
    }

    fun methodCallTransaction(
        appId: Long,
        account: Account,
        method: Method,
        methodArgs: List<Any?>? = null,
        foreignApps: List<Long>? = null,
        boxReferences: List<AppBoxReference>? = null
    ): AtomicTransactionComposer.ExecuteResult? {
        try {
            algodClient?.let {
                val rsp = it.TransactionParams().execute()
                val tsp: TransactionParametersResponse = rsp.body()

                val mctb = MethodCallTransactionBuilder.Builder()
                mctb.applicationId(appId)
                mctb.sender(account.address)
                mctb.signer(account.transactionSigner)
                mctb.method(method)
                methodArgs?.let { mctb.methodArguments(methodArgs) }
                foreignApps?.let { mctb.foreignApps(foreignApps) }
                boxReferences?.let { mctb.boxReferences(boxReferences) }
                mctb.onComplete(Transaction.OnCompletion.NoOpOC)
                mctb.suggestedParams(tsp)

                val atc = AtomicTransactionComposer()
                atc.addMethodCall(mctb.build())

                return atc.execute(algodClient, ROUNDS_TO_WAIT)
            }
        } catch (e: Exception) {
            Log.e(TAG, "" + e.toString())
        }
        return null
    }

    suspend fun getCurrentRound(): Long {
        var output = 0L
        withContext(Dispatchers.IO) {
            try {
                algodClient?.let {
                    val params: TransactionParametersResponse =
                        algodClient.TransactionParams().execute().body()
                    output = params.lastRound
                }
            } catch (e: Exception) {
                output = 0L
            }
        }
        return output
    }

    suspend fun fetchValidatorInfo(
        validatorId: Long,
    ) {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            val methodArgs = listOf(validatorId)
            val boxReferences = listOf(
                AppBoxReference(0, getValidatorListBoxName(validatorId)),
                AppBoxReference(0, "ignore".toByteArray())
            )
            val method = contract.getMethodByName("getValidatorConfig")

            account?.let {
                result = methodCallTransaction(
                    appId = RETI_APP_ID_TESTNET,
                    account = account,
                    method = method,
                    methodArgs = methodArgs,
                    boxReferences = boxReferences
                )
            }
        }

        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            return
        } else {
            val validatorConfig = validatorConfigFromABIReturn(result?.methodResults?.get(0)?.value as Any)

            validatorConfig?.let {
                try {
                    var valid: Valid? = null
                    val nfdRecord = if (validatorConfig.NFDForInfo.toInt() != 0) {
                        valid = with(nfdApi.isValid(validatorConfig.NFDForInfo.toInt())) {
                            val body = body()
                            if (isSuccessful && body != null) {
                                body()
                            } else {
                                null
                            }
                        }
                        if (valid?.name != null) {
                            with(nfdApi.fetchNfd(valid.name)) {
                                val body = body()
                                if (isSuccessful && body != null) {
                                    body()
                                } else {
                                    null
                                }
                            }
                        } else {
                            null
                        }
                    } else {
                        null
                    }

                    val avatar = nfdRecord?.properties?.userDefined?.avatar
                        ?: (if (valid?.name?.contains(".algo") == true) {
                        "https://app.nf.domains/img/nfd-image-placeholder.jpg"
                    } else {
                        null
                    })

                    var algoStaked = BigInteger.ZERO
                    var apy = BigInteger.ZERO
                    var lastPayout = BigInteger.ZERO

                    val validatorPools = getValidatorPools(
                        validatorId = validatorId
                    )
                    validatorPools.forEach {
                        algoStaked += it.totalAlgoStaked.toBigInteger()
                        val validatorAppGlobalState = getPoolAppGlobalState(
                            validatorPoolId = it.poolAppId
                        )
                        apy += validatorAppGlobalState.ewma
                        if (validatorAppGlobalState.lastPayout > lastPayout)
                            lastPayout = validatorAppGlobalState.lastPayout

                        it.algodVersion = validatorAppGlobalState.algodVersion
                        it.ewma = validatorAppGlobalState.ewma.toLong()
                        it.lastPayout = validatorAppGlobalState.lastPayout.toLong()
                        it.poolId = validatorAppGlobalState.poolId.toLong()
                        insertValidatorPoolIntoDb(it)
                    }

                    val calculatedApy = if (validatorPools.count() > 0) {
                        apy.toBigDecimal()
                            .divide(validatorPools.count().toBigDecimal(), 3, RoundingMode.HALF_UP)
                            .divide(TEN_K.toBigDecimal(), 3, RoundingMode.HALF_UP)
                    } else {
                        0.0
                    }

                    val validatorEntity = Validator(
                        id = validatorId,
                        name = valid?.name ?: validatorConfig.Owner.toString(),
                        algoStaked = algoStaked.toLong(),
                        apy = calculatedApy.toDouble(),
                        avatar = avatar,
                        percentToValidator = validatorConfig.PercentToValidator.toLong(),
                        epochRoundLength = validatorConfig.EpochRoundLength.toLong(),
                        minEntryStake = validatorConfig.MinEntryStake.toLong(),
                        inactive = false,
                        lastPayout = lastPayout.toLong()
                    )
                    insertValidatorIntoDb(validatorEntity)
                    Log.e(TAG, "update validator $validatorId info in DB")
                } catch (e: Exception) {
                    // ignore if network error
                }
            }
        }
    }

    fun validatorConfigFromABIReturn(returnVal: Any): ValidatorConfig? {
        return try {
            val arrReturn = returnVal as? Array<*>
                ?: throw IllegalArgumentException("unknown value returned from abi, " +
                        "type:${returnVal.javaClass.simpleName}")

            if (arrReturn.size != 18) {
                throw IllegalArgumentException("should be 18 elements " +
                        "returned in ValidatorConfig response")
            }

            val config = ValidatorConfig()
            config.ID = (arrReturn[0] as BigInteger)
            config.Owner = (arrReturn[1] as Address)
            config.Manager = (arrReturn[2] as Address)
            config.NFDForInfo = (arrReturn[3] as BigInteger)
            config.EntryGatingType = (arrReturn[4] as BigInteger)
            config.EntryGatingAddress = (arrReturn[5] as Address)
            config.EntryGatingAssets = (arrReturn[6] as Array<*>)
            config.GatingAssetMinBalance = (arrReturn[7] as BigInteger)
            config.RewardTokenId = (arrReturn[8] as BigInteger)
            config.RewardPerPayout = (arrReturn[9] as BigInteger)
            config.EpochRoundLength = (arrReturn[10] as BigInteger)
            config.PercentToValidator = (arrReturn[11] as BigInteger)
            config.ValidatorCommissionAddress = (arrReturn[12] as Address)
            config.MinEntryStake = (arrReturn[13] as BigInteger)
            config.MaxAlgoPerPool = (arrReturn[14] as BigInteger)
            config.PoolsPerNode = (arrReturn[15] as BigInteger)
            config.SunsettingOn = (arrReturn[16] as BigInteger)
            config.SunsettingTo = (arrReturn[17] as BigInteger)

            config
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getValidatorPools(
        validatorId: Long,
    ): MutableList<ValidatorPool> {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            val methodArgs = listOf(validatorId)
            val boxReferences = listOf(
                AppBoxReference(0, getValidatorListBoxName(validatorId)),
                AppBoxReference(0, "ignore".toByteArray())
            )
            val method = contract.getMethodByName("getPools")

            account?.let {
                result = methodCallTransaction(
                    appId = RETI_APP_ID_TESTNET,
                    account = account,
                    method = method,
                    methodArgs = methodArgs,
                    boxReferences = boxReferences
                )
            }
        }

        val output = mutableListOf<ValidatorPool>()
        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            // do nothing for now
        } else {
            val returnVal = result?.methodResults?.get(0)?.value as? Array<*>
            if (returnVal is Array<*>) {
                for (poolInfoAny in returnVal) {
                    if (poolInfoAny is Array<*>) {
                        val pool = ValidatorPool()
                        pool.poolAppId = (poolInfoAny[0] as BigInteger).toLong()
                        pool.validatorId = validatorId
                        pool.totalStakers = (poolInfoAny[1] as BigInteger).toLong()
                        pool.totalAlgoStaked = (poolInfoAny[2] as BigInteger).toLong()

                        // added in next call
                        pool.algodVersion = ""
                        pool.ewma = 0L
                        pool.lastPayout = 0L
                        pool.poolId = 0
                        output.add(pool)
                    }
                }
            }
        }
        return output
    }

    suspend fun getPoolAppGlobalState(
        validatorPoolId: Long
    ): ValidatorAppGlobalState {
        var result = ValidatorAppGlobalState()
        withContext(Dispatchers.IO) {
            try {
                val app = algodClient?.GetApplicationByID(validatorPoolId)?.execute()
                val globalKeys = app?.body()?.params?.globalState
                globalKeys?.forEach {
                    val decodedKey = String(Base64.getDecoder().decode(it.key))
                    when (decodedKey) {
                        "algodVer" -> {
                            val decodedValue = Base64.getDecoder().decode(it.value.bytes.toString())
                            result.algodVersion = String(decodedValue)
                        }

                        "ewma" -> {
                            val decodedValue = Base64.getDecoder().decode(it.value.bytes.toString())
                            result.ewma = BigInteger(decodedValue)
                        }

                        "lastPayout" -> {
                            result.lastPayout = it.value.uint
                        }

                        "poolId" -> {
                            result.poolId = it.value.uint
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        return result
    }

    suspend fun createTransactionWithSigner(
        account: Account,
        appId: Long,
        amount: Int,
    ): TransactionWithSigner? =
        withContext(Dispatchers.IO) {
            try {
                algodClient?.let {
                    // Get suggested params from client
                    val rsp: Response<TransactionParametersResponse> =
                        algodClient.TransactionParams().execute()
                    val sp: TransactionParametersResponse = rsp.body() ?: rsp.body() ?: rsp.body()

                    // Create a transaction
                    val ptxn =
                        PaymentTransactionBuilder
                            .Builder()
                            .suggestedParams(sp)
                            .amount(amount)
                            .sender(account.address)
                            .receiver(Address.forApplication(appId))
                            .build()

                    // Construct TransactionWithSigner
                    val tws = TransactionWithSigner(ptxn, account.transactionSigner)
                    tws
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                null
            }
        }

    suspend fun addValidatorStake(
        validatorId: Long,
        poolAppId: Long,
        amount: Int
    ): Long {
        // val gas = calculateValidatorStakeGas(validatorId, contractStr)
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            try {
                account?.let {
                    val tws = createTransactionWithSigner(account, RETI_APP_ID_TESTNET, amount)
                    val methodArgs = listOf(tws, validatorId.toBigInteger(), BigInteger.ZERO)
                    val foreignApps = listOf(poolAppId)
                    val boxReferences = listOf(
                        AppBoxReference(poolAppId, getStakerLedgerBoxName()),
                        AppBoxReference(0, "ignore".toByteArray()),
                        AppBoxReference(0, "ignore".toByteArray()),
                        AppBoxReference(0, "ignore".toByteArray()),
                        AppBoxReference(0, "ignore".toByteArray()),
                        AppBoxReference(0, "ignore".toByteArray()),
                        AppBoxReference(0, "ignore".toByteArray()),
                        AppBoxReference(0, "ignore".toByteArray()),
                    )
                    val method = contract.getMethodByName("addStake")
                    algodClient?.let {
                        val rsp = it.TransactionParams().execute()
                        val tsp: TransactionParametersResponse = rsp.body()
                        tsp.fee = tsp.minFee.times(TWO_HUNDRED_FORTY).times(2)

                        val mctb = MethodCallTransactionBuilder.Builder()
                        mctb.applicationId(RETI_APP_ID_TESTNET)
                        mctb.sender(account.address)
                        mctb.signer(account.transactionSigner)
                        mctb.method(method)
                        mctb.methodArguments(methodArgs)
                        mctb.foreignApps(foreignApps)
                        mctb.boxReferences(boxReferences)
                        mctb.onComplete(Transaction.OnCompletion.NoOpOC)
                        mctb.suggestedParams(tsp)

                        val atc = AtomicTransactionComposer()
                        atc.addMethodCall(mctb.build())

                        result = atc.execute(algodClient, ROUNDS_TO_WAIT)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

        val output = 0L
        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            // do nothing for now
        } else {
            val returnVal = result?.methodResults?.get(0)?.value as? Array<*>
        }
        return output
    }

    suspend fun calculateValidatorStakeGas(
        validatorId: Long
    ): Long {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            account?.let {
                val methodArgs = null
                val boxReferences = listOf(
                    AppBoxReference(0, getValidatorListBoxName(validatorId)),
                    AppBoxReference(0, "ignore".toByteArray()),
                    AppBoxReference(0, "ignore".toByteArray()),
                    AppBoxReference(0, "ignore".toByteArray()),
                    AppBoxReference(0, "ignore".toByteArray()),
                    AppBoxReference(0, "ignore".toByteArray()),
                    AppBoxReference(0, getStakerPoolSetBoxName(account.address))
                )
                val method = contract.getMethodByName("gas")

                result = methodCallTransaction(
                    appId = RETI_APP_ID_TESTNET,
                    account = account,
                    method = method,
                    methodArgs = methodArgs,
                    boxReferences = boxReferences
                )
            }
        }

        val output = 0L
        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            // do nothing for now
        } else {
            val returnVal = result?.methodResults?.get(0)?.value as? Array<*>
        }
        return output
    }

    suspend fun getStakedPoolsForAccount(
        accountAddress: String
    ): List<ValidatorPool> {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            account?.let {
                val methodArgs = listOf(account.address)
                val boxReferences = null
                val method = contract.getMethodByName("getStakedPoolsForAccount")

                result = methodCallTransaction(
                    appId = RETI_APP_ID_TESTNET,
                    account = account,
                    method = method,
                    methodArgs = methodArgs,
                    boxReferences = boxReferences
                )
            }
        }

        val output = listOf<ValidatorPool>()
        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            // do nothing for now
        } else {
            val returnVal = result?.methodResults?.get(0)?.value as? Array<*>
        }
        return output
    }
}
