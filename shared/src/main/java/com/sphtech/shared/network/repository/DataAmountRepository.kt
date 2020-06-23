package com.sphtech.shared.network.repository

import com.sphtech.shared.core.base.BaseAPIRepository
import com.sphtech.shared.core.result.Results
import com.sphtech.shared.entities.DataAmountResponse
import com.sphtech.shared.network.ApiService
import javax.inject.Inject

class DataAmountRepository @Inject constructor(
    private val service: ApiService,
    private val baseAPIRepository: BaseAPIRepository
) {

    suspend fun callDataAmountAPI(resourceId:String, limit:Int): Results<DataAmountResponse> =
        baseAPIRepository.safeApiCall(
            call = {
                service.callDataAmountAPIAsync(
                    resourceId ,limit
                ).await()
            },
            errorMessage = "Error occurred"
        )
}