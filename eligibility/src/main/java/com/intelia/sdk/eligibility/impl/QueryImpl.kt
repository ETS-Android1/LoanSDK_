package com.intelia.sdk.eligibility.impl

import android.content.Context
import com.intelia.sdk.eligibility.models.Eligibility
import com.intelia.sdk.eligibility.models.RelevantApps
import com.intelia.sdk.eligibility.models.SmsDataPoint
import com.intelia.sdk.eligibility.usecase.QueryUsecase
import io.reactivex.Observable
import org.json.JSONObject

open class QueryImpl : QueryUsecase {

    private constructor()

    private lateinit var context: Context
    private lateinit var apiKey: String
    private lateinit var name: String
    private lateinit var extras: JSONObject
    private var maxSms: Int = 0

    internal constructor(
        context: Context,
        name: String,
        key: String,
        maxSMS: Int = 500,
        extras: JSONObject
    ) : this() {
        this.context = context
        this.apiKey = key
        this.name = name
        this.extras = extras
        this.maxSms = maxSMS
    }


    private val queryImplementation = QueryImplementation()

    override fun calculateEligibility(): Observable<Eligibility> {
        return queryImplementation.calculateEligibility(context, name, apiKey, extras, maxSms)
    }

    override fun smsData(): Observable<MutableList<SmsDataPoint>> {
        return queryImplementation.smsData(context, maxSms)
    }

    override fun relevantApp(): RelevantApps {
        return queryImplementation.relevantApp()
    }

    override fun isRelevantSms(sms: String): Boolean {
        return queryImplementation.isRelevantSms(sms)
    }

    override fun isRelevantApp(packageName: String): Boolean {
        return queryImplementation.isRelevantApp(packageName)
    }
}