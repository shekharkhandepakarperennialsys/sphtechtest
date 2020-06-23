package com.sphtech.shared.core.base

import com.sphtech.shared.core.result.Results
import com.sphtech.shared.network.repository.prefs.SharedPreferenceStorage
import com.sphtech.shared.util.ConstantsBase
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BaseAPIRepository @Inject constructor() {
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Results<T> = try {
        val response = call.invoke()

        if (response.isSuccessful && response.code() == ConstantsBase.STATUS_SUCCESS) {
            Results.Success(response.body()!!)
        } else if (response.errorBody() != null && (response.code() == ConstantsBase.INTERNAL_ERROR || response.code() == ConstantsBase.BAD_REQUEST_ERROR
                    || response.code() == ConstantsBase.FORBIDDEN_ERROR || response.code() == ConstantsBase.UNAUTHORIZED_ERROR)
        ) {
            val messageObject = response.errorBody()
            try {
                val obj = JSONObject(messageObject!!.string())  // error handling in json here
                val errorToShow =
                    (obj.getJSONArray("errors")[0] as JSONObject).get("title") as String
                Results.Error(IOException(errorToShow))
            } catch (t: Throwable) {
                Results.Error(IOException("Something went wrong..."))
            }
        } else {
            Results.Error(IOException("Something went wrong..."))
        }
    } catch (e: Exception) {
        Results.Error(IOException(errorMessage, e))
    }

    val <T> T.exhaustive: T get() = this
}