package com.algorand.android.ui.staking.ui.staked

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorand.android.models.ValidatorPool
import com.algorand.android.repository.ValidatorRespository
import com.algorand.android.usecase.AccountAlgoAmountUseCase
import com.algorand.android.usecase.AccountDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ValidatorStakePoolsUIState {
    object Loading : ValidatorStakePoolsUIState()

    data class Error(
        val message: String,
    ) : ValidatorStakePoolsUIState()

    data class Success(
        val result: List<ValidatorPool>,
    ) : ValidatorStakePoolsUIState()
}

private const val TIMEOUT: Long = 250L
private const val STOP_TIMEOUT: Long = 5000L
private const val TAG: String = "ValidatorStakeAmountViewModel"

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
open class ValidatorStakedPoolsViewModel @Inject constructor(
    private val accountAlgoAmountUseCase: AccountAlgoAmountUseCase,
    private val accountDetailUseCase: AccountDetailUseCase,
    private val validatorRepository: ValidatorRespository,
) : ViewModel() {
    var accountAddress = "unknown"

    var validatorStakedPoolsUIStateFlow =
        MutableStateFlow<ValidatorStakePoolsUIState>(ValidatorStakePoolsUIState.Loading)
    var validatorStakedPoolsUIState = validatorStakedPoolsUIStateFlow.asStateFlow()

    var stakedPoolsStateFlow = MutableStateFlow<List<ValidatorPool>>(listOf())
    var stakedPoolsState = stakedPoolsStateFlow.asStateFlow()

    fun refreshStakedPoolsForAccount(accountAddress: String) {
        viewModelScope.launch {
            val stakedPools = validatorRepository.getStakedPoolsForAccount(
                accountAddress = "LMRZRYVM3EXPLNGW7VRIIZ4CQKW5IVPDDXCKFZKCLJF35CNJMEKJNUTJIQ",
            )
            Log.e(TAG, "number of stakedPool is ${stakedPools.size}")
        }
    }
}
