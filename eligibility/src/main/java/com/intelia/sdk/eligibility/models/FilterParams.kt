package com.intelia.sdk.eligibility.models

object FilterParams {
    val query = mutableListOf(

        DataPointCategory(
            "Airtime Recharge",
            mutableListOf("^.*txn.*\$"),
            DataPointType.SMS
        ),
        DataPointCategory(
            "Debit",
            mutableListOf(
                "^.* dr.*\$",
                "^.*debit.*\$",
                "^.*debited.*\$",
                "^.*dr amt:.*\$",
                "^.*debit.*\$",
                "^.*debit:ngn.*\$"
            ),
            DataPointType.SMS
        ),
        DataPointCategory(
            "Credit",
            mutableListOf(
                "^. cr.*\$",
                "^.*credit.*\$",
                "^.*credited.*\$",
                "^.*cr amt:.*\$",
                "^.*credit.*\$",
                "^.*credit:ngn.*\$"
            ),
            DataPointType.SMS
        )
    )
}