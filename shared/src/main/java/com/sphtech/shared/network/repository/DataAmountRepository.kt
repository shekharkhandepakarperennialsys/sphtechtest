package com.sphtech.shared.network.repository

import com.sphtech.shared.core.base.BaseRepository
import com.sphtech.shared.core.result.Results
import com.sphtech.shared.entities.DataAmountResponse
import com.sphtech.shared.network.ApiService
import javax.inject.Inject

class DataAmountRepository @Inject constructor(
    private val service: ApiService,
    private val baseRepository: BaseRepository
) {

    suspend fun callDataAmountAPI(resourceId:String, limit:Int): Results<DataAmountResponse> =
        baseRepository.safeApiCall(
            call = {
                service.callDataAmountAPIAsync(
                    resourceId ,limit
                ).await()
            },
            errorMessage = "Error occurred"
        )
}