package com.intelia.sdk.eligibility.impl

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
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
import retrofit2.HttpException
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
                }.flatMap { list ->
                    context.buildDeviceInfo(extras)
                    val extraMapping = hashMapOf<String, String>()
                    extras.keys().forEach {
                        extraMapping[it] = extras.getString(it)
                    }
                    val api =
                        ApiClient.retrofitSigned(response.key!!).create(AnalysisApi::class.java)
                    api.calculateEligibility(
                        name,
                        response.key!!,
                        DataRequest(list, extraMapping)
                    )
                }.onErrorReturn {
                    val eligibility = NetworkResponses.DataPointResponse()
                    (it as? HttpException)?.response()?.errorBody()?.string()?.let {
                        val errorrMessage = JSONObject(it)
                        val errorObj = JsonObject()
                        errorObj.addProperty("errorCode", errorrMessage.optString("error-code"))
                        errorObj.addProperty("message", errorrMessage.optString("message"))
                        eligibility["errorMessage"] = errorObj

                    } ?: kotlin.run {
                        val errorObj = JsonObject()
                        errorObj.addProperty("message", "error occured processing request")
                        eligibility["errorMessage"] = errorObj
                    }
                    eligibility
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