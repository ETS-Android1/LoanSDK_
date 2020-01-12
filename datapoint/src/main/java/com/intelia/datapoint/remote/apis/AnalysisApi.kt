package com.intelia.datapoint.remote.apis

import com.intelia.datapoint.remote.NetworkResponses
import com.intelia.datapoint.models.DataRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AnalysisApi {

    @POST("/calculate_eligible_amount")
    fun calculateEligibility(@Body data: DataRequest): Observable<NetworkResponses.DataPointResponse>


}