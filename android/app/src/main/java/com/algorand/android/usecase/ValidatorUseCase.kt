package com.algorand.android.usecase

import com.algorand.android.models.Validator
import com.algorand.android.repository.ValidatorRespository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ValidatorUseCase @Inject constructor(
    private val validatorRespository: ValidatorRespository
) {

    val coroutineScope = CoroutineScope(Dispatchers.Default)

    private fun loadSearchDataIntoDb() {
        val validatorList: List<Validator> =
            listOf(
                Validator(
                    id = 1L,
                    name = "reti.algo",
                    algoStaked = 25000L,
                    apy = 3.03,
                    avatar = "https://app.nf.domains/img/nfd-image-placeholder.jpg",
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5,
                    lastPayout = 41367026L
                ),
                Validator(
                    id = 2L,
                    name = "steventest.algo",
                    algoStaked = 25000L,
                    apy = 3.03,
                    avatar = "https://app.nf.domains/img/nfd-image-placeholder.jpg",
                    percentToValidator = 1,
                    epochRoundLength = 32000,
                    minEntryStake = 1000,
                    lastPayout = 41367026L
                ),
                Validator(
                    id = 3L,
                    name = "algoleaguestestnet.algo",
                    algoStaked = 25000L,
                    apy = 3.03,
                    avatar = "https://app.nf.domains/img/nfd-image-placeholder.jpg",
                    percentToValidator = 5,
                    epochRoundLength = 10000,
                    minEntryStake = 10,
                    lastPayout = 41367026L
                )
            )

        coroutineScope.launch {
            validatorList.forEach {
                validatorRespository.insertValidatorIntoDb(it)
            }
        }
    }
}
