package com.sphtech.shared.network

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ApiService @Inject constructor(
    builder: Retrofit.Builder
) {
    var baseUrl = "https://data.gov.sg/"

    var apiClient =
        builder.baseUrl(baseUrl)
            .build()
            .create(ApiClient::class.java)

    fun callDataAmountAPIAsync(resourceId:String, limit: Int) = apiClient.callDataAmount(resourceId, limit)

}
