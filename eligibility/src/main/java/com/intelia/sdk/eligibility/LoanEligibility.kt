package com.intelia.sdk.eligibility

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.intelia.sdk.eligibility.impl.QueryImpl
import org.json.JSONObject

object LoanEligibility {

    fun init(context: Context, name: String, apiKey: String,extras : JSONObject): QueryImpl {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        )
            throw Exception("android.Manifest.permission.READ_SMS permission is required")
        return QueryImpl(context, name, apiKey,extras)
    }

}