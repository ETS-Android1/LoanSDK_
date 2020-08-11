package com.intelia.sdk.eligibility

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.intelia.sdk.eligibility.impl.QueryImpl
import org.json.JSONObject

object LoanEligibility {
    const val SMS_MAX = 500
    fun init(
        context: Context,
        name: String,
        apiKey: String,
        extras: JSONObject,
        maxSms: Int = SMS_MAX
    ): QueryImpl {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        )
            throw Exception("android.Manifest.permission.READ_SMS permission is required")
        return QueryImpl(context, name, apiKey, maxSms, extras)
    }

}