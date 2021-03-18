package com.intelia.sdk.eligibility.impl

import android.content.Context
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
import io.reactivex.ObservableEmitter
import org.json.JSONObject
import retrofit2.HttpException
import java.util.*
import java.util.regex.Pattern


internal class QueryImplementation(val api: AnalysisApi = ApiClient.retrofit.create(AnalysisApi::class.java)) {


    private fun updateProgress(
        progress: Int,
        message: String,
        emitter: ObservableEmitter<Eligibility>
    ) {
        val eligibility = NetworkResponses.DataPointResponse()
        val errorObj = JsonObject()
        errorObj.addProperty("progress", progress)
        errorObj.addProperty("progressMessage", message)
        eligibility["progressUpdate"] = errorObj
        emitter.onNext(eligibility)
    }

    private fun calculateEligibilityEmittingProgress(
        context: Context,
        name: String,
        apiKey: String,
        extras: JSONObject,
        maxSms: Int,
        emitter: ObservableEmitter<Eligibility>
    ): Observable<Eligibility> {
        val eligibility = NetworkResponses.DataPointResponse()
        val errorObj = JsonObject()
        updateProgress(10, "verifying Key", emitter)
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
                updateProgress(30, "Key Verified", emitter)
                Observable.just(response)
            } ?: run {
                updateProgress(30, "Generating new key", emitter)
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
            updateProgress(60, "Analysing user data", emitter)
            SmsQuery().smsSearch(context, maxSms)
                .map { it ->
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
                }.map { it ->
                    updateProgress(80, "Completing data analysis", emitter)
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
                    updateProgress(90, "Syncing data to loan engine", emitter)
                    context.buildDeviceInfo(extras)
                    val extraMapping = hashMapOf<String, String>()
                    extras.keys().forEach {
                        extraMapping[it] = extras.getString(it)
                    }
                    val api =
                        ApiClient.retrofitSigned(response.key!!).create(AnalysisApi::class.java)
                    api.calculateEligibility(
                        name,
                        response.key,
                        DataRequest(list, extraMapping)
                    )
                }.onErrorReturn { it ->
                    // this handles error performing the request
                    (it as? HttpException)?.response()?.errorBody()?.string()?.let {
                        val errorMessage = JSONObject(it)
                        //assign error message to key values for user
                        errorObj.addProperty("errorCode", errorMessage.optString("error-code"))
                        errorObj.addProperty("message", errorMessage.optString("message"))
                        eligibility["errorMessage"] = errorObj

                    } ?: kotlin.run {
                        //assign error message when message is null or ""
                        errorObj.addProperty("message", "An error occurred while processing your request")
                        eligibility["errorMessage"] = errorObj
                    }
                    eligibility
                }
                .map {
                    it
                }
        }
    }

    fun calculateEligibility(
        context: Context,
        name: String,
        apiKey: String,
        extras: JSONObject,
        maxSMS: Int
    ): Observable<Eligibility> {
        return Observable.create { emitter ->
            emitter.onNext(
                calculateEligibilityEmittingProgress(
                    context,
                    name,
                    apiKey,
                    extras,
                    maxSMS,
                    emitter
                ).blockingFirst()
            )
            emitter.onComplete()
        }
    }

    fun smsData(context: Context, maxSMS: Int): Observable<MutableList<SmsDataPoint>> {
        return SmsQuery().smsSearch(context, maxSMS)
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