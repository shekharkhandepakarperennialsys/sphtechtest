package com.sphtech.shared.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class Interceptor @Inject constructor() :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val mainRequest = chain.request()
        val builder = mainRequest.newBuilder()
            .header("x-api-key", "")
            .method(mainRequest.method, mainRequest.body)

        return chain.proceed(builder.build())
    }
}