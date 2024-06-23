package com.algorand.android.ui.staking.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorand.android.models.Validator
import com.algorand.android.repository.ValidatorRespository
import com.algorand.android.usecase.AccountDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ValidatorsListUIState {
    object Loading : ValidatorsListUIState()

    data class Error(
        val message: String,
    ) : ValidatorsListUIState()

    data class Success(
        val result: List<Validator>,
    ) : ValidatorsListUIState()
}

private const val TIMEOUT: Long = 250L
private const val STOP_TIMEOUT: Long = 5000L
private const val TAG: String = "ValidatorSearchViewModel"

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
open class ValidatorSearchViewModel @Inject constructor(
    private val accountDetailUseCase: AccountDetailUseCase,
    private val validatorRepository: ValidatorRespository,
) : ViewModel() {
        var accountAddress = "unknown"

        var abiContract = "ValidatorRegistry.arc4.json"

        val validatorsFromDb =
            validatorRepository
                .getAllValidatorsFromDb()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT), emptyList())

        val searchQuery = MutableStateFlow("")
        val validatorsListUIState: StateFlow<ValidatorsListUIState> =
            searchQuery
                .debounce(TIMEOUT)
                .flatMapLatest { searchQuery ->
                    validatorsFromDb.mapLatest { validatorList: List<Validator> ->
                        if (validatorList.isNotEmpty()) {
                            val validators =
                                validatorList
                                    .filter { it.name.contains(searchQuery, ignoreCase = true) }
                                    .sortedByDescending { it.apy }
                            ValidatorsListUIState.Success(validators)
                        } else {
                            ValidatorsListUIState.Success(listOf())
                        }
                    }
                }.stateIn(viewModelScope, SharingStarted.Lazily, ValidatorsListUIState.Loading)

        fun onValidatorSearchQueryChange(query: String) {
            searchQuery.value = query
        }

        fun setupDB() {
            viewModelScope.launch {
                val account = accountDetailUseCase.getAccount(accountAddress)
                account?.apply {
                    val validatorCount =
                        validatorRepository.getNumberOfValidators()
                    Log.e(TAG, "validatorCount is $validatorCount")
                    for (validatorId in 1..validatorCount) {
                        Log.e(TAG, "fetch validator $validatorId info")
                        validatorRepository.fetchValidatorInfo(
                            validatorId
                        )
                    }
                }
            }
        }
    }
