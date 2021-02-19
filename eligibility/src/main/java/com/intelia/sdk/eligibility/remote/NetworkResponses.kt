package com.intelia.sdk.eligibility.remote

import com.intelia.sdk.eligibility.models.Eligibility
import com.intelia.sdk.eligibility.models.IpRequest


sealed class NetworkResponses {
    class DataPointResponse : Eligibility()
    class KeyResponse : IpRequest()
}