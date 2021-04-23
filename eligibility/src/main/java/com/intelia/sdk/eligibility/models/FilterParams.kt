package com.intelia.sdk.eligibility.models

object FilterParams {
    val query = mutableListOf(

        DataPointCategory(
            "Airtime Recharge",
            mutableListOf("^.*airtime.*\$", "^.*recharge.*\$", "^.*top.*\$",),
            DataPointType.SMS
        ),
        DataPointCategory(
            "Debit Transaction",
            mutableListOf(
                "^.*debit alert!.*\$",
                "^.*dr amt:.*\$",
                "^.*amt:.*dr.*\$",
                "^.*debit:ngn.*\$",
                "^.*dr.*\$",
                "^.*debit.*\$",
                "^.*debited.*\$",
            ),
            DataPointType.SMS
        ),
        DataPointCategory(
            "Credit Transaction",
            mutableListOf(
                "^.credit alert!.*\$",
                "^.*cr amt:.*\$",
                "^.*amt:.*cr.*\$",
                "^.*credit:ngn.*\$",
                "^.*cr.*\$",
                "^.*credit.*\$",
                "^.*credited.*\$",
            ),
            DataPointType.SMS
        )
    )
}