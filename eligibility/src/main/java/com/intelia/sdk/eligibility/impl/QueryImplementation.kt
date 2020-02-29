package com.intelia.sdk.eligibility.impl

import android.content.Context
import com.intelia.sdk.eligibility.ext.buildDeviceInfo
import com.intelia.sdk.eligibility.ext.hash
import com.intelia.sdk.eligibility.ext.ip
import com.intelia.sdk.eligibility.models.*
import com.intelia.sdk.eligibility.remote.ApiClient
import com.intelia.sdk.eligibility.remote.NetworkResponses
import com.intelia.sdk.eligibility.remote.apis.AnalysisApi
import com.intelia.sdk.eligibility.repository.SmsQuery
import io.reactivex.Observable
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern


internal class QueryImplementation(val api: AnalysisApi = ApiClient.retrofit.create(AnalysisApi::class.java)) {


    fun calculateEligibility(
        context: Context,
        name: String,
        apiKey: String,
        extras: JSONObject
    ): Observable<Eligibility> {
        return api.retrieve(
            IpRequest(
                name,
                apiKey,
                context.ip(),
                context.hash(),
                context.packageName
            )
        ).onErrorReturn {
            NetworkResponses.KeyResponse()
        }.flatMap { response ->
                response.key?.let {
                    Observable.just(response)
                } ?: run {
                    api.generateKey(
                        IpRequest(
                            name,
                            apiKey,
                            context.ip(),
                            context.hash(),
                            context.packageName
                        )
                    )
                }
        }.flatMap { response ->
                SmsQuery().smsSearch(context)
                    .map {
                        val body = mutableListOf<Request>()
                        it.forEach { sdp ->
                            body.addAll(sdp.sms.map {
                                Request(
                                    sdp.category,
                                    it.number,
                                    it.body,
                                    it.date,
                                    true
                                )
                            })
                        }
                        body
                    }.map {
                        it.addAll(relevantApp().apps.map {
                            Request(
                                "",
                                it,
                                "",
                                Date(),
                                false
                            )
                        })
                        it
                    }.flatMap {
                        context.buildDeviceInfo(extras)
                        api.calculateEligibility(
                            name,
                            response.key!!,
                            DataRequest(it,extras)
                        )
                    }
                    .map {
                        it
                    }
            }

    }

    fun smsData(context: Context): Observable<MutableList<SmsDataPoint>> {
        return SmsQuery().smsSearch(context)
    }

    fun relevantApp(): RelevantApps {
        return RelevantApps(mutableListOf())
    }

    fun isRelevantSms(sms: String): Boolean {
        var matches = false
        FilterParams.query.forEach {
            if (matches)
                return matches
            it.contentFilter.forEach {
                if (matches)
                    return matches
                val p = Pattern.compile(it)
                val m = p.matcher(sms.toLowerCase())
                matches = m.matches()

            }
        }
        return matches
    }

    fun isRelevantApp(packageName: String): Boolean {
        return false
    }
}