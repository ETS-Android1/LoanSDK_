
package com.intelia.sdk.eligibility.cordova

object PluginActions {
    const val ELIGIBILITY: String = "ELIGIBILITY" // to perform eligibility check
    const val TRANSACTIONAL_DATA: String =
        "TRANSACTIONAL_DATA" //  to return transactional sms on user device
    const val IS_TRANSACTIONAL_SMS: String =
        "IS_TRANSACTIONAL_SMS" // to check if sms or string content is regcognized as transactional by the engine
    const val IS_RELEVANT_APP: String =
        "IS_RELEVANT_APP" // to check if the application qualifies to be assessed for eligility check
    const val RELEVANT_APPS: String =
        "RELEVANT_APPS" // check if device actively uses relevant application that can affect eligibility check.
}