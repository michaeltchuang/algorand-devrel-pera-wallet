package com.algorand.android.ui.staking

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.algorand.android.R
import com.algorand.android.core.BaseFragment
import com.algorand.android.databinding.FragmentValidatorSearchBinding
import com.algorand.android.models.FragmentConfiguration
import com.algorand.android.ui.staking.navigation.stakingNavigation
import com.algorand.android.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ValidatorSearchFragment"
@AndroidEntryPoint
class ValidatorSearchFragment : BaseFragment(R.layout.fragment_validator_search) {

    override val fragmentConfiguration = FragmentConfiguration()

    private val binding by viewBinding(FragmentValidatorSearchBinding::bind)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val accountAddress = arguments?.getString("accountAddress")
        if (accountAddress != null) {
            binding.composeViewValidatorSearch.setContent {
                stakingNavigation(accountAddress, onNavigateBackToFragment = {
                    findNavController().popBackStack()
                }) // Home for Feature (Compose UI)
            }
        } else {
            // error handling for later
            Log.e(TAG, "THere should always be a valid account address on this screen")
        }
    }
}
