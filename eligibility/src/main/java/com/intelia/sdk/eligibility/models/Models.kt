package com.intelia.sdk.eligibility.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
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
    val data: MutableList<Request>,
    val extras: HashMap<String,String>
)

open class Eligibility(
    val eligible: Boolean = false,
    @SerializedName("expense-rate") val expense_rate: Double = 0.0,
    @SerializedName("hold-money-rate") val hold_money_rate: Double = 0.0,
    @SerializedName("risk-profile") val risk_profile: Double = 0.0,
    @SerializedName("airtime-borrow-rate") val airtime_borrow_rate: Double = 0.0,
    @SerializedName("airtime-expense-rate") val airtime_expense_rate: Double = 0.0,
    @SerializedName("average-monthly-strength") val average_monthly_strength: Double = 0.0,
    @SerializedName("loan_count") val loan_count: Double = 0.0,
    @SerializedName("phone-app-risk-profile") val phone_app_risk_profile: Double = 0.0,
    @SerializedName("previously-taken-loan-amount") val previously_taken_loan_amount: Double = 0.0,
    @SerializedName("previously-taken-loan-rate") val previously_taken_loan_rate: Double = 0.0,
    @SerializedName("recommended-loan-amount") val recommended_loan_amount: Double = 0.0,
    @SerializedName("repayment-confidence") val repayment_confidence: Double = 0.0,
    @SerializedName("salary-amount") val salary_amount: Double = 0.0,
    @SerializedName("salary-earner-discount-factor") val salary_earner_discount_factor: Double = 0.0,
    val status: Int = 0,
    @SerializedName("is-loan-taker") val is_loan_taker: Boolean = false,
    @SerializedName("is-salary-earner") val is_salary_earner: Boolean = false,
    @SerializedName("freq-used-acc-numbers") val freq_used_acc_numbers: MutableList<UserBankAcct> = mutableListOf()
): TreeMap<String, JsonElement>()


data class SmsDataPoint(val category: String, var sms: MutableList<Sms>)

data class UserBankAcct(val bank: String,
                        var ending_number: String)

data class RelevantApps(var apps: MutableList<String>)



