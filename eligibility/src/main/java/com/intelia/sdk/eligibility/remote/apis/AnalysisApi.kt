package com.intelia.sdk.eligibility.remote.apis

import com.intelia.sdk.eligibility.models.DataRequest
import com.intelia.sdk.eligibility.models.IpRequest
import com.intelia.sdk.eligibility.remote.NetworkResponses
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalysisApi {

    @POST("/api/v1/request/calculate_eligible_amount")
    fun calculateEligibility(@Body data: DataRequest): Observable<NetworkResponses.DataPointResponse>

    @POST("/api/v1/auth//auth/generate_key")
    fun generateKey(@Body data: IpRequest): Observable<NetworkResponses.KeyResponse>

    @POST("/api/v1/auth//auth/retrieve_key")
    fun retrieve(@Body data: IpRequest): Observable<NetworkResponses.KeyResponse>


}