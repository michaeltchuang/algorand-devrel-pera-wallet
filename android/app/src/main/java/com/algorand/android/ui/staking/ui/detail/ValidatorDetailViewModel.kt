package com.algorand.android.ui.staking.ui.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.algorand.android.core.BaseViewModel
import com.algorand.android.models.Validator
import com.algorand.android.models.ValidatorPool
import com.algorand.android.modules.assets.profile.about.domain.usecase.GetSelectedAssetExchangeValueUseCase
import com.algorand.android.repository.ValidatorRespository
import com.algorand.android.usecase.AccountAlgoAmountUseCase
import com.algorand.android.utils.AlgoAssetInformationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

sealed class ValidatorDetailUIState {
    object Loading : ValidatorDetailUIState()

    data class Error(
        val message: String,
    ) : ValidatorDetailUIState()

    data class Success(
        val result: Validator,
    ) : ValidatorDetailUIState()
}

private const val TAG: String = "ValidatorsListViewModel"

@HiltViewModel
class ValidatorDetailViewModel @Inject constructor(
    private val validatorRepository: ValidatorRespository,
    private val accountAlgoAmountUseCase: AccountAlgoAmountUseCase,
    private val algoAssetInformationProvider: AlgoAssetInformationProvider,
    private val getSelectedAssetExchangeValueUseCase: GetSelectedAssetExchangeValueUseCase,
) : BaseViewModel() {
    var accountAddress = "unknown"
    var stakeAmount = BigDecimal.ZERO

    var validatorDetailUIStateFlow = MutableStateFlow<ValidatorDetailUIState>(ValidatorDetailUIState.Loading)
    var validatorDetailUIState = validatorDetailUIStateFlow.asStateFlow()

    var accountAlgoAmountStateFlow = MutableStateFlow<BigInteger>(BigInteger.ZERO)
    var accountAlgoAmountState = accountAlgoAmountStateFlow.asStateFlow()

    var stakeAlgoTextFieldStateFlow = MutableStateFlow<String>("")
    var stakeAlgoTextFieldState = stakeAlgoTextFieldStateFlow.asStateFlow()

    var validatorPoolsStateFlow = MutableStateFlow<List<ValidatorPool>>(listOf())
    var validatorPoolsState = validatorPoolsStateFlow.asStateFlow()

    var currentRoundStateFlow = MutableStateFlow<Long>(0L)
    var currentRoundState = currentRoundStateFlow.asStateFlow()

    fun fetchValidatorById(validatorId: Long) {
        viewModelScope.launch {
            validatorRepository
                .getValidatorByIdFromDb(validatorId).collect { validator ->
                    Log.i(TAG, "validator result is $validator")
                    if (validator == null) {
                        validatorDetailUIStateFlow.value =
                            ValidatorDetailUIState.Error("Validator does not exist")
                    } else {
                        validatorDetailUIStateFlow.value = ValidatorDetailUIState.Success(validator)
                    }
                }
        }
    }

    fun fetchValidatorPools(validatorId: Long) {
        viewModelScope.launch {
            validatorRepository.getAllValidatorPoolsFromDb(validatorId).collect { pools ->
                validatorPoolsStateFlow.value = pools
            }
        }
    }

    fun fetchMaxAccountAlgo() {
        viewModelScope.launch {
            val accountAlgoAmount = accountAlgoAmountUseCase.getAccountAlgoAmount(accountAddress)
            accountAlgoAmountStateFlow.value = accountAlgoAmount.amount
        }
    }

    fun refreshValidatorData(validatorId: Long) {
        viewModelScope.launch {
            validatorRepository.fetchValidatorInfo(
                validatorId = validatorId,
            )
        }
    }

    fun getCurrentRound() {
        viewModelScope.launch {
            val round = validatorRepository.getCurrentRound()
            Log.d(TAG, "Current Round: $round")
            currentRoundStateFlow.value = round
        }
    }

    fun getAlgoUSDPrice(stakeAlgos: BigDecimal): String {
        val assetDetail = algoAssetInformationProvider.getAlgoAssetInformation().data
        val assetPrice = getSelectedAssetExchangeValueUseCase.getSelectedAssetExchangeValue(assetDetail)
        return if (assetPrice == null) {
                "Unknown Price"
            } else {
                val stakingUsdAmount = assetPrice.amountAsCurrency.multiply(stakeAlgos)
                "${assetPrice.selectedCurrencySymbol}${stakingUsdAmount.setScale(2, RoundingMode.HALF_EVEN)}"
            }
    }

    fun addStakeToValidator(
        validatorId: Long,
        poolAppId: Long,
        stakeAmount: Int
    ) {
        viewModelScope.launch {
            val result =
                validatorRepository.addValidatorStake(
                    validatorId = validatorId,
                    poolAppId = poolAppId,
                    amount = stakeAmount
                )
        }
    }
}
