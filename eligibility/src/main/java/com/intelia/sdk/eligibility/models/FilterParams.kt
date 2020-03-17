package com.intelia.sdk.eligibility.models

object FilterParams {
    val query = mutableListOf(

        DataPointCategory(
            "Airtime Recharge",
            mutableListOf("^.*txn.*\$"),
            DataPointType.SMS
        ),
        DataPointCategory(
            "Debit Transaction",
            mutableListOf(
                "^.*debit alert!.*\$",
                "^.*dr amt:.*\$",
                "^.*debit:ngn.*\$"
            ),
            DataPointType.SMS
        ),
        DataPointCategory(
            "Credit Transaction",
            mutableListOf(
                "^. cr.*\$",
                "^.*cr amt:.*\$",
                "^.*credit:ngn.*\$"
            ),
            DataPointType.SMS
        )
    )
}