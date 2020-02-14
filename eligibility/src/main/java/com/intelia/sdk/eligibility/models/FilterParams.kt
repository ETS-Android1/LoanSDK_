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
            "Credit Transaction",
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