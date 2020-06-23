package com.sphtech.shared.network

import com.sphtech.shared.entities.DataAmountResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("api/action/datastore_search")
    fun callDataAmount(
        @Query("resource_id") resourceId: String,
        @Query("limit") limit: Int
    ): Deferred<Response<DataAmountResponse>>
}