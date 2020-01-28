package com.intelia.sdk.eligibility.remote.apis

import com.intelia.sdk.eligibility.remote.NetworkResponses
import com.intelia.sdk.eligibility.models.DataRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalysisApi {

    @POST("/calculate_eligible_amount")
    fun calculateEligibility(@Body data: DataRequest): Observable<NetworkResponses.DataPointResponse>


}