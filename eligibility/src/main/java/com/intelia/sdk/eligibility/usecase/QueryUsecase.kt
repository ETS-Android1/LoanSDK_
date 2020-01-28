package com.intelia.sdk.eligibility.usecase

import com.intelia.sdk.eligibility.models.Eligibility
import com.intelia.sdk.eligibility.models.RelevantApps
import com.intelia.sdk.eligibility.models.SmsDataPoint
import io.reactivex.Observable

interface QueryUsecase {
    fun smsData(): Observable<MutableList<SmsDataPoint>>

    fun calculateEligibility(): Observable<Eligibility>

    fun relevantApp(): RelevantApps

    fun isRelevantSms(sms: String): Boolean

    fun isRelevantApp(packageName: String): Boolean
}