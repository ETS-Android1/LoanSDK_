@file:Suppress("UNCHECKED_CAST")

package com.intelia.loansdk.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelia.sdk.eligibility.LoanEligibility
import com.intelia.loansdk.App
import org.json.JSONObject

object VMFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainVM(
            LoanEligibility.init(
                App.appContext,
                "sam_5432",
                "sample_key",
                JSONObject()
            )
        ) as T
    }
}
