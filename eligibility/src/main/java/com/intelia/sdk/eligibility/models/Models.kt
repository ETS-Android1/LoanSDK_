package com.intelia.sdk.eligibility.models

import com.google.gson.annotations.SerializedName
import java.util.*


enum class DataPointType {
    SMS, APP
}

data class DataPointCategory(
    val category: String,
    val contentFilter: MutableList<String>,
    val dataPointType: DataPointType
)

data class Sms(val number: String, val body: String, var date: Date)

data class Request(
    val category: String,
    val title: String,
    val data: String,
    var date: Date,
    var isSms: Boolean
)


open class IpRequest(
    val name: String = "",
    val apiKey: String = "",
    val ip_address: String = "",
    val sha1: String = "",
    val packageName: String = "",
    val key: String? = null
)




data class DataRequest(
    val data: MutableList<Request>
)

open class Eligibility(
    val eligible: Boolean = false,
    @SerializedName("expense-rate") val expense_rate: Double = 0.0,
    @SerializedName("hold-money-rate") val hold_money_rate: Double = 0.0,
    @SerializedName("risk-profile") val risk_profile: Double = 0.0,
    val status: Int = 0
)


data class SmsDataPoint(val category: String, var sms: MutableList<Sms>)

data class RelevantApps(var apps: MutableList<String>)



