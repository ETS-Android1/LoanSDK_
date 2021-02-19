package com.intelia.sdk.eligibility.remote.apis

import com.intelia.sdk.eligibility.models.DataRequest
import com.intelia.sdk.eligibility.models.IpRequest
import com.intelia.sdk.eligibility.remote.NetworkResponses
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AnalysisApi {

    @POST("/api/v1/request/calculate_eligibility")
    fun calculateEligibility(
        @Query("name") name: String,
        @Query("key") key: String,
        @Body data: DataRequest
    ): Observable<NetworkResponses.DataPointResponse>

    @POST("/api/v1/auth/generate_key")
    fun generateKey(@Body data: IpRequest): Observable<NetworkResponses.KeyResponse>

    @POST("/api/v1/auth/retrieve_key")
    fun retrieve(@Body data: IpRequest): Observable<NetworkResponses.KeyResponse>


}